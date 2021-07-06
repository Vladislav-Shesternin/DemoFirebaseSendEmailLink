package com.example.demofirebasesendemaillink

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.demofirebasesendemaillink.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks

class MainActivity : AppCompatActivity() {

    companion object {
        const val FIREBASE_DEFAULT_DOMAIN = "https://demofirebasesendemaillink.firebaseapp.com"
    }

    private lateinit var binding: ActivityMainBinding
    private val auth = FirebaseAuth.getInstance()

    var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData(intent)

        binding.btnSend.setOnClickListener {
            val email = binding.editEmail.text.toString()

            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                auth.sendSignInLinkToEmail(email, getActionCodeSettings())
                    .addOnSuccessListener {
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failure: $it", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Not Email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getData(intent: Intent) {
        FirebaseDynamicLinks.getInstance().getDynamicLink(intent)
            .addOnSuccessListener {
                if (it != null && it.link != null) {
                    email = it.link!!.getEmail()
                }
            }
    }

    private fun Uri.getEmail(): String? {
        return getQueryParameter("continueUrl")
            ?.toUri()
            ?.getQueryParameter("email")
    }

    private fun getActionCodeSettings() = actionCodeSettings {
        url = "$FIREBASE_DEFAULT_DOMAIN" + "/?email=your_email@gmail.com"
        handleCodeInApp = true
        setIOSBundleId("com.example.ios")
        setAndroidPackageName(BuildConfig.APPLICATION_ID, true, null)
    }

}

//   /?email=$email






