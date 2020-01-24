package com.trackme.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        sign_in_button.setOnClickListener {
            signIn()
            progressBar31.setVisibility(View.VISIBLE)
        }

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser: FirebaseUser? = auth.currentUser
        updateUI(currentUser, null)
    }

    fun logIn(view: View) {

        val email: String = textInputLayout.toString().trim()
        val password: String = textInputLayout2.toString().trim()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) { // Sign in success, update UI with the signed-in user's information
                        Log.d("email auth", "createUserWithEmail:success")
                        val user: FirebaseUser? = auth.currentUser
                        updateUI(user, null)
                    } else { // If sign in fails, display a message to the user.
                        Log.w(
                            "email auth",
                            "createUserWithEmail:failure",
                            task.exception
                        )
                        Toast.makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null, null)
                    }
                    // ...
                })
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // [START_EXCLUDE]
                updateUI(null, null)
                // [END_EXCLUDE]
            }
        }
    }



    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)
        // [START_EXCLUDE silent]
        // showProgressDialog()
        progressBar31.setVisibility(View.VISIBLE)
        // [END_EXCLUDE]

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    var status=3
                    updateUI(user,status)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Snackbar.make(login, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    updateUI(null, null)
                }

                // [START_EXCLUDE]
                //hideProgressDialog()
                progressBar31.visibility = View.INVISIBLE
                // [END_EXCLUDE]
            }
    }

    private fun updateUI(user: FirebaseUser?, status: Int?) {
        progressBar31.visibility = View.INVISIBLE
        if (user != null) {
            var intent:Intent= Intent(applicationContext,MapActivity::class.java)
            intent.putExtra("email",user.email)
            intent.putExtra("uid",user.uid)
            intent.putExtra("personPhoto",user.photoUrl)
            intent.putExtra("name",user.displayName)
            intent.putExtra("mob",user.phoneNumber)
            Log.v("sssss",user.email)
            startActivity(intent)

        } else {

        } }
}
