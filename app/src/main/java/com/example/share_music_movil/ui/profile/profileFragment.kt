package com.example.share_music_movil.ui.profile

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.volley.toolbox.Volley
import com.example.share_music_movil.Connectors.UserService
import com.example.share_music_movil.R
import com.example.share_music_movil.VolleyCallBack


class profileFragment : Fragment() {

    private lateinit var ProfileViewModel: profileViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        ProfileViewModel =
            ViewModelProviders.of(this).get(profileViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val textView: TextView = root.findViewById(R.id.text_profile)
        ProfileViewModel.text.observe(viewLifecycleOwner, Observer {
            var msharedPreferences = context?.getSharedPreferences("SPOTIFY", 0);
            var queue = Volley.newRequestQueue(context);
            var userService= msharedPreferences?.let { it1 -> UserService(queue, it1) }
            userService?.get(object : VolleyCallBack {
                @SuppressLint("SetTextI18n")
                override fun onSuccess() {
                    val user = userService.user?.display_name
                    textView.text = "This is profile of $user"
                }
            })
        })
        return root
    }
}
