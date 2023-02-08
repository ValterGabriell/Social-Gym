package com.valtergabriel.desafiolealapps.repo

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.valtergabriel.desafiolealapps.dto.User
import com.valtergabriel.desafiolealapps.ui.FeedActivity
import com.valtergabriel.desafiolealapps.util.Constants.COLLECTION_USER_NAME
import com.valtergabriel.desafiolealapps.util.Constants.PASSWORD_HANDLE
import com.valtergabriel.desafiolealapps.util.Firebase
import java.time.LocalDateTime

class UserRepository {


    suspend fun createNewUser(user: User, context: Context) {
        val pass = user.password + PASSWORD_HANDLE
        Firebase.getAuth().createUserWithEmailAndPassword(user.email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userAuthenticated = Firebase.getAuth().currentUser
                    val userData = hashMapOf(
                        "email" to userAuthenticated?.email,
                        "id" to userAuthenticated?.uid,
                        "creationDate" to LocalDateTime.now().toString(),
                        "age" to user.age,
                        "weight" to user.weight,
                        "expectedWeight" to user.expectedWeight,
                        "height" to user.height
                    )

                    Firebase.getFirestore().collection(COLLECTION_USER_NAME)
                        .document(userAuthenticated?.uid.toString())
                        .set(userData).also {
                            Intent(context, FeedActivity::class.java).also {
                                context.startActivity(it)
                            }
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Falha ao criar usuário", Toast.LENGTH_SHORT).show()
            }
    }

    suspend fun signInUser(email:String, password:String, context: Context, btn: Button, progressBar:ProgressBar) {
        val pass = password + PASSWORD_HANDLE
        Firebase.getAuth().signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Intent(context, FeedActivity::class.java).also {
                        context.startActivity(it)
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Falha ao logar usuário, tente novamente", Toast.LENGTH_SHORT).show()
                btn.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
    }




}