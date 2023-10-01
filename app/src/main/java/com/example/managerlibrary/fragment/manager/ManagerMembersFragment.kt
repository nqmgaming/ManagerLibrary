package com.example.managerlibrary.fragment.manager

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.managerlibrary.adapter.MemberAdapter
import com.example.managerlibrary.adapter.Top10Adapter
import com.example.managerlibrary.dao.MemberDAO
import com.example.managerlibrary.dto.MemberDTO
import com.example.managerlibrary.databinding.FragmentManagerMembersBinding
import com.example.managerlibrary.dto.BookDTO
import com.example.managerlibrary.ui.manager.AddCategoryBooksActivity
import com.example.managerlibrary.ui.manager.AddMemberActivity
import com.example.managerlibrary.viewmodel.SharedViewModel


class ManagerMembersFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var adapter: MemberAdapter
    lateinit var listMember: ArrayList<MemberDTO>
    lateinit var memberDAO: MemberDAO

    private lateinit var sharedViewModel: SharedViewModel

    private var _binding: FragmentManagerMembersBinding? = null
    private val binding get() = _binding!!

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

        if (!listMember.isEmpty()) {
            adapter = MemberAdapter(requireContext(), listMember)
            binding.managerMembersRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        val data = arguments?.getString("ok")
        if (data.equals("member")) {
            refresh()
        }
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        sharedViewModel.searchText.observe(viewLifecycleOwner, Observer { newText ->
            // Cập nhật giao diện hoặc thực hiện tìm kiếm với `newText`
            var filterList = ArrayList<MemberDTO>()
            for (member in listMember) {
                if (member.name.contains(newText, ignoreCase = true)) {
                    filterList.add(member)
                }
            }

            //update to adapter
            binding.managerMembersRecyclerView.adapter = MemberAdapter(requireContext(), filterList)
            adapter.notifyDataSetChanged()

        })

        binding.fabMembersBill.setOnClickListener(){
            Intent(requireContext(), AddMemberActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    fun refresh(){
        listMember.clear()
        listMember.addAll(memberDAO.getAllMember())
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}