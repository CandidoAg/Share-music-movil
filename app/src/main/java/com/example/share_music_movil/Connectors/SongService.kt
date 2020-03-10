package com.example.share_music_movil.Connectors

import Model.Song
import android.content.Context
import android.content.SharedPreferences
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.share_music_movil.VolleyCallBack
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class SongService(context: Context) {
    val songs = ArrayList<Song>()
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("SPOTIFY", 0)
    private val queue: RequestQueue = Volley.newRequestQueue(context)

    fun getRecentlyPlayedTracks(callBack: VolleyCallBack): ArrayList<Song> {
        val endpoint = "https://api.spotify.com/v1/me/player/recently-played"
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET,
            endpoint,
            null,
            Response.Listener { response: JSONObject ->
                val gson = Gson()
                val jsonArray = response.optJSONArray("items")
                for (n in 0 until jsonArray.length()) {
                    try {
                        var JSONObject = jsonArray.getJSONObject(n)
                        JSONObject = JSONObject.optJSONObject("track")
                        val song = gson.fromJson(JSONObject.toString(), Song::class.java)
                        songs.add(song)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                callBack.onSuccess()
            },
            Response.ErrorListener { error: VolleyError? -> }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> =
                    HashMap()
                val token = sharedPreferences.getString("token", "")
                val auth = "Bearer $token"
                headers["Authorization"] = auth
                return headers
            }
        }
        queue.add(jsonObjectRequest)
        return songs
    }

    fun addSongToLibrary(song: Song) {
        val payload = preparePutPayload(song)
        val jsonObjectRequest = prepareSongLibraryRequest(payload)
        queue.add(jsonObjectRequest)
    }

    private fun prepareSongLibraryRequest(payload: JSONObject): JsonObjectRequest {
        return object : JsonObjectRequest(
            Method.PUT,
            "https://api.spotify.com/v1/me/tracks",
            payload,
            Response.Listener { response: JSONObject? -> },
            Response.ErrorListener { error: VolleyError? -> }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> =
                    HashMap()
                val token = sharedPreferences.getString("token", "")
                val auth = "Bearer $token"
                headers["Authorization"] = auth
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
    }

    private fun preparePutPayload(song: Song): JSONObject {
        val idarray = JSONArray()
        idarray.put(song.id)
        val ids = JSONObject()
        try {
            ids.put("ids", idarray)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return ids
    }

}