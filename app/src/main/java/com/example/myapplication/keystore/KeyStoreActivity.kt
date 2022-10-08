package com.example.myapplication.keystore

import android.app.KeyguardManager
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityKeyStoreBinding
import com.example.myapplication.utils.Utils
import java.security.InvalidAlgorithmParameterException
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class KeyStoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKeyStoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKeyStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFingerPrintSettinga()
        intiUIAndListeners()
    }

    private fun intiUIAndListeners() {
        binding.btnSaveInKeystore.setOnClickListener {
            storePasswordAndLogin()
        }
        binding.tvLoginWithFingerPrint.setOnClickListener { loginWithFingerPrint() }
    }

    private fun loginWithFingerPrint() {
        val encryptedData = Utils.getStringFromSharedPrefs(this, "encryptedData")
        val encryptedIv = Utils.getStringFromSharedPrefs(this, "encryptedIv")

        val encryptedIvBytes = Base64.decode(encryptedIv, Base64.DEFAULT)
        val encryptedDataBytes = Base64.decode(encryptedData, Base64.DEFAULT)

        val keystore = KeyStore.getInstance("AndroidKeyStore")
        keystore.load(null)
        val secretKey = keystore.getKey("my_fk_key", null) as SecretKey

        val cipher =
            Cipher.getInstance(
                KeyProperties.KEY_ALGORITHM_AES + "/" +
                        KeyProperties.BLOCK_MODE_CBC + "/" +
                        KeyProperties.ENCRYPTION_PADDING_PKCS7
            )

        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(encryptedIvBytes))
        val dataBytes = cipher.doFinal(encryptedDataBytes)
        val data = String(dataBytes)
        Utils.saveStringInSharedPrefs(this@KeyStoreActivity, "Data", data)

    }

    private fun storePasswordAndLogin() {
        try {
            val data = binding.etData.text.toString().trim()
            val secretKey = createSecretKey()
            val cipher =
                Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/" +
                            KeyProperties.BLOCK_MODE_CBC + "/" +
                            KeyProperties.ENCRYPTION_PADDING_PKCS7
                )

            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val encryptedIv = cipher.iv
            val dataBytes = data.toByteArray(Charsets.UTF_8)
            val encodedDataBytes = cipher.doFinal(dataBytes)
            val encryptedData = Base64.encodeToString(encodedDataBytes, Base64.DEFAULT)
            Utils.saveStringInSharedPrefs(this@KeyStoreActivity, "encryptedData", encryptedData)
            Utils.saveStringInSharedPrefs(
                this@KeyStoreActivity,
                "encryptedIv",
                Base64.encodeToString(encryptedIv, Base64.DEFAULT)
            )
        }
        catch (e: Exception){
            Log.d("TAG", "storePasswordAndLogin: ${e.message}")
        }
    }

    private fun createSecretKey(): SecretKey {
        try {
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val keySpecs = KeyGenParameterSpec.Builder(
                "my_fk_key",
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setUserAuthenticationRequired(false)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build()
            keyGenerator.init(keySpecs)
            return keyGenerator.generateKey()
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to create a symmetric key $e")
        } catch (e: NoSuchProviderException) {
            throw RuntimeException("Failed to create a symmetric key $e")
        } catch (e: InvalidAlgorithmParameterException) {
            throw RuntimeException("Failed to create a symmetric key $e")
        }
    }

    private fun initFingerPrintSettinga() {
        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        if (keyguardManager.isKeyguardSecure.not()) {
            Toast.makeText(
                this@KeyStoreActivity,
                "Secure lock screen has not been setup",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(this@KeyStoreActivity, "Screen is secured", Toast.LENGTH_SHORT).show()
        }
    }
}