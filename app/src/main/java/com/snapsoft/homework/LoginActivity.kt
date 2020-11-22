package com.snapsoft.homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.snapsoft.homework.api.ServiceBuilder
import com.snapsoft.homework.api.TmdbEndpoints
import com.snapsoft.homework.model.DTOs.RequestTokenDTO
import com.snapsoft.homework.model.DTOs.SessionDTO
import com.snapsoft.homework.model.SessionRequestBody
import com.snapsoft.homework.model.UserValidator
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var requestToken :String
    val request = ServiceBuilder.buildService(TmdbEndpoints::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        intent.getStringExtra("request_token")?.let {
            requestToken = it
        }

        btn_login.setOnClickListener {
            login()
        }
    }

    private fun generateSession(newRequestToken: String) {
        val sessionCall = request.generateValidatedSession(SessionRequestBody(newRequestToken), getString(R.string.api_key))

        sessionCall.enqueue(object : Callback<SessionDTO> {
            override fun onResponse(call: Call<SessionDTO>, response: Response<SessionDTO>) {
                response.body()?.let {
                    val sessionId = response.body()!!.session_id
                    Toast.makeText(this@LoginActivity, "SessionID: $sessionId", Toast.LENGTH_LONG).show()
                } ?: run {
                    Toast.makeText(this@LoginActivity, "Authentication failed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<SessionDTO>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun login() {
        val username = et_username.text.toString()
        val password = et_password.text.toString()
        val userData = UserValidator(username, password, requestToken)
        val validateCall = request.validateWithLogin(userData, getString(R.string.api_key))

        validateCall.enqueue(object : Callback<RequestTokenDTO> {
            override fun onResponse(call: Call<RequestTokenDTO>, response: Response<RequestTokenDTO>) {
                response.body()?.let {
                    requestToken = it.request_token
                    Toast.makeText(this@LoginActivity, "New request token: $requestToken", Toast.LENGTH_SHORT).show()
                    generateSession(requestToken)
                } ?: run {
                    Toast.makeText(this@LoginActivity, "Authentication failed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<RequestTokenDTO>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}
