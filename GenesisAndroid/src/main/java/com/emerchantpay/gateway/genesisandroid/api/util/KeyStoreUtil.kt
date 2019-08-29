package com.emerchantpay.gateway.genesisandroid.api.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.support.annotation.RequiresApi
import android.util.Base64
import android.util.Log
import com.emerchantpay.gateway.genesisandroid.api.constants.SharedPrefConstants
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.security.*
import java.security.cert.CertificateException
import java.util.*
import javax.crypto.*
import javax.crypto.spec.GCMParameterSpec
import javax.security.auth.x500.X500Principal

class KeyStoreUtil(private val mContext: Context?) {

    private val secretKeyFromSharedPreferences: Int
        get() {
            val sharedPreferences = GenesisSharedPreferences()
            return sharedPreferences.getInt(mContext, SharedPrefConstants.CONSUMER_ID)
        }

    private val secretKeyAPIMorGreater: Key
        @Throws(CertificateException::class, NoSuchAlgorithmException::class, IOException::class, KeyStoreException::class, UnrecoverableKeyException::class)
        get() {
            val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE_NAME)
            keyStore.load(null)
            return keyStore.getKey(KEY_ALIAS, null)

        }

    // Using algorithm as described at https://medium.com/@ericfu/securely-storing-secrets-in-an-android-application-501f030ae5a3
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun initKeys() {
        var keyStore: KeyStore? = null
        try {
            keyStore = KeyStore.getInstance(ANDROID_KEY_STORE_NAME)
        } catch (e: KeyStoreException) {

        }

        try {
            keyStore!!.load(null)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        }

        try {
            if (!keyStore!!.containsAlias(KEY_ALIAS)) {
                try {
                    initValidKeys()
                } catch (e: NoSuchAlgorithmException) {
                    e.printStackTrace()
                } catch (e: NoSuchProviderException) {
                    e.printStackTrace()
                } catch (e: InvalidAlgorithmParameterException) {
                    e.printStackTrace()
                } catch (e: CertificateException) {
                    e.printStackTrace()
                } catch (e: UnrecoverableEntryException) {
                    e.printStackTrace()
                } catch (e: NoSuchPaddingException) {
                    e.printStackTrace()
                } catch (e: KeyStoreException) {
                    e.printStackTrace()
                } catch (e: InvalidKeyException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            } else {
                var keyValid = false
                try {
                    val keyEntry = keyStore.getEntry(KEY_ALIAS, null)
                    if (keyEntry is KeyStore.SecretKeyEntry && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        keyValid = true
                    }

                    if (keyEntry is KeyStore.PrivateKeyEntry && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        val secretKey = secretKeyFromSharedPreferences
                        // When doing "Clear data" on Android 4.x it removes the shared preferences (where
                        // we have stored our encrypted secret key) but not the key entry. Check for existence
                        // of key here as well.
                        if (secretKey > 0) {
                            keyValid = true
                        }
                    }
                } catch (e: NullPointerException) {
                    // Bad to catch null pointer exception, but looks like Android 4.4.x
                    // pin switch to password Keystore bug.
                    // https://issuetracker.google.com/issues/36983155
                    Log.e(LOG_TAG, "Failed to get key store entry", e)
                } catch (e: UnrecoverableKeyException) {
                    Log.e(LOG_TAG, "Failed to get key store entry", e)
                } catch (e: NoSuchAlgorithmException) {
                    e.printStackTrace()
                } catch (e: KeyStoreException) {
                    e.printStackTrace()
                } catch (e: UnrecoverableEntryException) {
                    e.printStackTrace()
                }

                if (!keyValid) {
                    synchronized(s_keyInitLock) {
                        // System upgrade or something made key invalid
                        try {
                            removeKeys(keyStore)
                        } catch (e: KeyStoreException) {
                            e.printStackTrace()
                        }

                        try {
                            initValidKeys()
                        } catch (e: NoSuchAlgorithmException) {
                            e.printStackTrace()
                        } catch (e: NoSuchProviderException) {
                            e.printStackTrace()
                        } catch (e: InvalidAlgorithmParameterException) {
                            e.printStackTrace()
                        } catch (e: CertificateException) {
                            e.printStackTrace()
                        } catch (e: UnrecoverableEntryException) {
                            e.printStackTrace()
                        } catch (e: NoSuchPaddingException) {
                            e.printStackTrace()
                        } catch (e: KeyStoreException) {
                            e.printStackTrace()
                        } catch (e: InvalidKeyException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                }

            }
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        }

    }

    @Throws(KeyStoreException::class)
    protected fun removeKeys(keyStore: KeyStore) {
        keyStore.deleteEntry(KEY_ALIAS)
        removeSavedSharedPreferences()
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Throws(NoSuchAlgorithmException::class, NoSuchProviderException::class, InvalidAlgorithmParameterException::class, CertificateException::class, UnrecoverableEntryException::class, NoSuchPaddingException::class, KeyStoreException::class, InvalidKeyException::class, IOException::class)
    private fun initValidKeys() {
        synchronized(s_keyInitLock) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                generateKeysForAPIMOrGreater()
            } else {
                generateKeysForAPILessThanM()
            }
        }
    }

    @SuppressLint("ApplySharedPref")
    private fun removeSavedSharedPreferences() {
        val sharedPreferences = mContext?.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val clearedPreferencesSuccessfully = sharedPreferences?.edit()!!.clear().commit()
        Log.d(LOG_TAG, String.format("Cleared secret key shared preferences `%s`", clearedPreferencesSuccessfully))
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Throws(NoSuchProviderException::class, NoSuchAlgorithmException::class, InvalidAlgorithmParameterException::class, CertificateException::class, UnrecoverableEntryException::class, NoSuchPaddingException::class, KeyStoreException::class, InvalidKeyException::class, IOException::class)
    private fun generateKeysForAPILessThanM() {
        // Generate a key pair for encryption
        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        end.add(Calendar.YEAR, 30)
        val spec = KeyPairGeneratorSpec.Builder(mContext)
                .setAlias(KEY_ALIAS)
                .setSubject(X500Principal("CN=$KEY_ALIAS"))
                .setSerialNumber(BigInteger.TEN)
                .setStartDate(start.time)
                .setEndDate(end.time)
                .build()
        val kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM_NAME, ANDROID_KEY_STORE_NAME)
        kpg.initialize(spec)
        kpg.generateKeyPair()

        saveEncryptedKey()
    }

    @SuppressLint("ApplySharedPref")
    @Throws(CertificateException::class, NoSuchPaddingException::class, InvalidKeyException::class, NoSuchAlgorithmException::class, KeyStoreException::class, NoSuchProviderException::class, UnrecoverableEntryException::class, IOException::class)
    private fun saveEncryptedKey() {
        val pref = GenesisSharedPreferences()
        var encryptedKeyBase64encoded: String? = pref.getInt(mContext, SharedPrefConstants.CONSUMER_ID).toString()
        if (encryptedKeyBase64encoded == null) {
            val key = ByteArray(16)
            val secureRandom = SecureRandom()
            secureRandom.nextBytes(key)
            val encryptedKey = rsaEncryptKey(key)
            encryptedKeyBase64encoded = Base64.encodeToString(encryptedKey, Base64.DEFAULT)
            pref.putInt(mContext, SharedPrefConstants.CONSUMER_ID, Integer.parseInt(encryptedKeyBase64encoded!!))
            val successfullyWroteKey = true
            if (successfullyWroteKey) {
                Log.d(LOG_TAG, "Saved keys successfully")
            } else {
                Log.e(LOG_TAG, "Saved keys unsuccessfully")
                throw IOException("Could not save keys")
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Throws(NoSuchAlgorithmException::class, NoSuchProviderException::class, InvalidAlgorithmParameterException::class)
    protected fun generateKeysForAPIMOrGreater() {
        val keyGenerator: KeyGenerator
        keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE_NAME)
        keyGenerator.init(
                KeyGenParameterSpec.Builder(KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        // NOTE no Random IV. According to above this is less secure but acceptably so.
                        .setRandomizedEncryptionRequired(false)
                        .build())
        // Note according to [docs](https://developer.android.com/reference/android/security/keystore/KeyGenParameterSpec.html)
        // this generation will also add it to the keystore.
        keyGenerator.generateKey()
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun encryptData(stringDataToEncrypt: String?): String {

        initKeys()

        if (stringDataToEncrypt == null) {
            throw IllegalArgumentException("Data to be decrypted must be non null")
        }

        var cipher: Cipher? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                cipher = Cipher.getInstance(AES_MODE_M_OR_GREATER)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: NoSuchPaddingException) {
                e.printStackTrace()
            }

            try {
                cipher!!.init(Cipher.ENCRYPT_MODE, secretKeyAPIMorGreater,
                        GCMParameterSpec(128, FIXED_IV))
            } catch (e: InvalidKeyException) {
                e.printStackTrace()
            } catch (e: InvalidAlgorithmParameterException) {
                e.printStackTrace()
            } catch (e: CertificateException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: KeyStoreException) {
                e.printStackTrace()
            } catch (e: UnrecoverableKeyException) {
                e.printStackTrace()
            }

        } else {
            try {
                cipher = Cipher.getInstance(AES_MODE_LESS_THAN_M)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: NoSuchPaddingException) {
                e.printStackTrace()
            }

            try {
                val secretKeyAPIMorGreater = KeyStore.getInstance(ANDROID_KEY_STORE_NAME) as Key
                cipher!!.init(Cipher.ENCRYPT_MODE, secretKeyAPIMorGreater,
                        GCMParameterSpec(128, FIXED_IV))
            } catch (e: InvalidKeyException) {
                // Since the keys can become bad (perhaps because of lock screen change)
                // drop keys in this case.
                removeKeys()
            } catch (e: IllegalArgumentException) {
                removeKeys()
            } catch (e: InvalidAlgorithmParameterException) {
                e.printStackTrace()
            } catch (e: KeyStoreException) {
                e.printStackTrace()
            }

        }

        var encodedBytes = ByteArray(0)
        try {
            encodedBytes = cipher!!.doFinal(stringDataToEncrypt.toByteArray(charset(CHARSET_NAME)))
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return Base64.encodeToString(encodedBytes, Base64.DEFAULT)

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun decryptData(encryptedData: String?): String? {

        initKeys()

        if (encryptedData == null) {
            throw IllegalArgumentException("Data to be decrypted must be non null")
        }

        val encryptedDecodedData = Base64.decode(encryptedData, Base64.DEFAULT)

        var cipher: Cipher? = null
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    cipher = Cipher.getInstance(AES_MODE_M_OR_GREATER)
                } catch (e: NoSuchAlgorithmException) {
                    e.printStackTrace()
                } catch (e: NoSuchPaddingException) {
                    e.printStackTrace()
                }

                try {
                    cipher!!.init(Cipher.DECRYPT_MODE, secretKeyAPIMorGreater, GCMParameterSpec(128, FIXED_IV))
                } catch (e: InvalidAlgorithmParameterException) {
                    e.printStackTrace()
                } catch (e: CertificateException) {
                    e.printStackTrace()
                } catch (e: NoSuchAlgorithmException) {
                    e.printStackTrace()
                } catch (e: KeyStoreException) {
                    e.printStackTrace()
                } catch (e: UnrecoverableKeyException) {
                    e.printStackTrace()
                }

            } else {
                val secretKeyAPIMorGreater = KeyStore.getInstance(ANDROID_KEY_STORE_NAME) as Key
                cipher = Cipher.getInstance(AES_MODE_M_OR_GREATER)
                cipher!!.init(Cipher.DECRYPT_MODE, secretKeyAPIMorGreater,
                        GCMParameterSpec(128, FIXED_IV))
            }
        } catch (e: InvalidKeyException) {
            // Since the keys can become bad (perhaps because of lock screen change)
            // drop keys in this case.
            removeKeys()
        } catch (e: IOException) {
            removeKeys()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        }

        var decodedBytes = ByteArray(0)
        try {
            decodedBytes = cipher!!.doFinal(encryptedDecodedData)
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        }

        try {
            return String(decodedBytes, charset(CHARSET_NAME))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return null
    }

    @Throws(KeyStoreException::class, CertificateException::class, NoSuchAlgorithmException::class, IOException::class, NoSuchProviderException::class, NoSuchPaddingException::class, UnrecoverableEntryException::class, InvalidKeyException::class)
    private fun rsaEncryptKey(secret: ByteArray): ByteArray {

        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE_NAME)
        keyStore.load(null)

        val privateKeyEntry = keyStore.getEntry(KEY_ALIAS, null) as KeyStore.PrivateKeyEntry
        val inputCipher = Cipher.getInstance(RSA_MODE, CIPHER_PROVIDER_NAME_ENCRYPTION_DECRYPTION_RSA)
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.certificate.publicKey)

        val outputStream = ByteArrayOutputStream()
        val cipherOutputStream = CipherOutputStream(outputStream, inputCipher)
        cipherOutputStream.write(secret)
        cipherOutputStream.close()

        return outputStream.toByteArray()
    }

    fun removeKeys() {
        synchronized(s_keyInitLock) {
            var keyStore: KeyStore? = null
            try {
                keyStore = KeyStore.getInstance(ANDROID_KEY_STORE_NAME)
            } catch (e: KeyStoreException) {
                e.printStackTrace()
            }

            try {
                keyStore!!.load(null)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: CertificateException) {
                e.printStackTrace()
            }

            try {
                keyStore?.let { removeKeys(it) }
            } catch (e: KeyStoreException) {
                e.printStackTrace()
            }

        }
    }

    companion object {
        private val ANDROID_KEY_STORE_NAME = "AndroidKeyStore"
        private val AES_MODE_M_OR_GREATER = "AES/GCM/NoPadding"
        private val AES_MODE_LESS_THAN_M = "AES/GCM/NoPadding"
        private val KEY_ALIAS = "GENESIS_KEY_ALIAS"
        // TODO update these bytes to be random for IV of encryption
        private val FIXED_IV = byteArrayOf(55, 54, 53, 52, 51, 50, 49, 48, 47, 46, 45, 44)
        private val CHARSET_NAME = "UTF-8"
        private val RSA_ALGORITHM_NAME = "RSA"
        private val RSA_MODE = "RSA/ECB/PKCS1Padding"
        private val CIPHER_PROVIDER_NAME_ENCRYPTION_DECRYPTION_RSA = "AndroidOpenSSL"
        private val SHARED_PREFERENCE_NAME = "GenesisAPI"
        private val LOG_TAG = KeyStoreUtil::class.java.name

        private val s_keyInitLock = Any()
    }
}
