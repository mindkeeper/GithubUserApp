package com.example.githubuserapp.view.detailuser

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.databinding.FragmentFollowListBinding
import com.example.githubuserapp.view.main.ListUserAdapter


class FollowListFragment : Fragment() {


    private var _binding : FragmentFollowListBinding? = null
    private val binding get() = _binding!!
    lateinit var state :String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribe()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        state = arguments?.getString("type").toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun subscribe() {
        val parent = activity as DetailUserActivity
        binding.rvUserList.layoutManager = LinearLayoutManager(parent)
        when(state){
            this.getString(R.string.follower) -> {
                parent.detailViewModel.listFollower.observe(parent){
                    binding.rvUserList.adapter = ListUserAdapter(it)
                }
            }
            this.getString(R.string.following) -> {
                parent.detailViewModel.listFollowing.observe(parent){
                    binding.rvUserList.adapter = ListUserAdapter(it)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(type: String): FollowListFragment =
            FollowListFragment().apply {
                arguments = Bundle().apply {
                    putString("type", type)
                }
            }
    }


}