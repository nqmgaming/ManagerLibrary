package com.example.managerlibrary.fragment.manager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.managerlibrary.MemberAdapter
import com.example.managerlibrary.MemberDAO
import com.example.managerlibrary.MemberDTO
import com.example.managerlibrary.R
import com.example.managerlibrary.databinding.FragmentManagerMembersBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ManagerMembersFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var adapter: MemberAdapter
    lateinit var listMember: ArrayList<MemberDTO>
    lateinit var memberDAO: MemberDAO

    private var _binding: FragmentManagerMembersBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentManagerMembersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.managerMembersRecyclerView.setHasFixedSize(true)
        binding.managerMembersRecyclerView.layoutManager = LinearLayoutManager(context)
        memberDAO = MemberDAO(requireContext())

        listMember = memberDAO.getAllMember()

        if (listMember.isEmpty()) {
            Toast.makeText(requireContext(), "List is empty", Toast.LENGTH_SHORT).show()
        } else {
            adapter = MemberAdapter(requireContext(), listMember)
            binding.managerMembersRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ManagerMembersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}