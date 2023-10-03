package com.example.managerlibrary.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.managerlibrary.R
import com.example.managerlibrary.databinding.ActivityMainBinding
import com.example.managerlibrary.fragment.account.AddNewUserFragment
import com.example.managerlibrary.fragment.account.ChangePasswordFragment
import com.example.managerlibrary.fragment.account.ProfileFragment
import com.example.managerlibrary.fragment.manager.ManagerBillsFragment
import com.example.managerlibrary.fragment.manager.ManagerBooksFragment
import com.example.managerlibrary.fragment.manager.ManagerCategoryBooksFragment
import com.example.managerlibrary.fragment.manager.ManagerMembersFragment
import com.example.managerlibrary.fragment.statistical.RevenueFragment
import com.example.managerlibrary.fragment.statistical.Top10Fragment
import com.example.managerlibrary.sharepre.LoginSharePreference
import com.example.managerlibrary.ui.account.LoginActivity
import com.example.managerlibrary.viewmodel.SharedViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private lateinit var loginSharePreference: LoginSharePreference
    private var currentFragment: Fragment? = null
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        setSupportActionBar(binding.toolbar)

        if (savedInstanceState == null) {

            val fragmentManager = supportFragmentManager
            currentFragment = ManagerBillsFragment()

            val fragmentTransaction = fragmentManager.beginTransaction()


            val defaultFragment = ManagerBillsFragment()
            fragmentTransaction.replace(R.id.nav_host_fragment, defaultFragment)
            supportActionBar?.title = resources.getString(R.string.manager_bill)

            fragmentTransaction.commit()
            invalidateOptionsMenu()
        }

        //get intent from add loan activity

        when (val data = intent.getStringExtra("ok")) {
            "ok" -> {
                val fragment = ManagerBillsFragment()
                val bundle = Bundle()
                bundle.putString("ok", "ok")
                fragment.arguments = bundle

                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit()
            }
            "category" -> {
                val fragment = ManagerCategoryBooksFragment()
                val bundle = Bundle()
                bundle.putString("ok", data)
                fragment.arguments = bundle

                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit()
            }
            "member" -> {
                val fragment = ManagerMembersFragment()
                val bundle = Bundle()
                bundle.putString("ok", data)
                fragment.arguments = bundle

                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit()
            }
            "bookOK" -> {
                val fragment = ManagerBooksFragment()
                val bundle = Bundle()
                bundle.putString("ok", data)
                fragment.arguments = bundle

                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit()
            }
        }


        //get username and user full name by share preference
        loginSharePreference = LoginSharePreference(this)
        val username = loginSharePreference.getID()
        val fullname = loginSharePreference.getName()
        val role = loginSharePreference.getRole()

        //set username and user full name to navigation header
        val headerView = binding.navView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.user_name)
        val userFullNameTextView = headerView.findViewById<TextView>(R.id.user_full_name)
        val userRole = headerView.findViewById<TextView>(R.id.txt_role)

        // Update the views with your data
        userNameTextView.text = "@$username"
        userFullNameTextView.text = fullname
        userRole.text = role

        //hide item menu add user if user is not admin
        if (role != "admin") {
            binding.navView.menu.findItem(R.id.add_user).isVisible = false
        }




        binding.navView.setNavigationItemSelectedListener { menuItem ->

            val fragmentManager = supportFragmentManager

            val fragmentTransaction = fragmentManager.beginTransaction()

            when (menuItem.itemId) {

                //Manager Bill
                R.id.manager_bill -> {
                    val fragment = ManagerBillsFragment()
                    currentFragment = fragment
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    supportActionBar?.title = resources.getString(R.string.manager_bill)
                }

                //Manager Book
                R.id.manager_book -> {
                    val fragment = ManagerBooksFragment()
                    currentFragment = fragment
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    supportActionBar?.title = resources.getString(R.string.manager_book)
                }

                //Manager User
                R.id.manager_user -> {
                    val fragment = ManagerMembersFragment()
                    currentFragment = fragment
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    supportActionBar?.title = resources.getString(R.string.manager_user)
                }

                //Manager Category Book
                R.id.manager_category -> {
                    val fragment = ManagerCategoryBooksFragment()
                    currentFragment = fragment
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    supportActionBar?.title = resources.getString(R.string.manager_category_book)
                }

                //Profile
                R.id.user_profile -> {
                    val fragment = ProfileFragment()
                    currentFragment = fragment
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    supportActionBar?.title = resources.getString(R.string.profile)
                }

                //Change Password
                R.id.change_password -> {
                    val fragment = ChangePasswordFragment()
                    currentFragment = fragment
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    supportActionBar?.title = resources.getString(R.string.change_password)
                }

                //Create new account
                R.id.add_user -> {
                    val fragment = AddNewUserFragment()
                    currentFragment = fragment
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    supportActionBar?.title = resources.getString(R.string.create_new_account)
                }

                //Top 10 book
                R.id.top10 -> {
                    val fragment = Top10Fragment()
                    currentFragment = fragment
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    supportActionBar?.title = resources.getString(R.string.top_10_book)
                }

                //Revenue
                R.id.money -> {
                    val fragment = RevenueFragment()
                    currentFragment = fragment
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    supportActionBar?.title = resources.getString(R.string.revenue)
                }

                //Logout
                R.id.logout -> {
                    //Aleart Dialog
                    val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                    builder.setTitle("Logout")
                    builder.setMessage("Do you want to logout?")
                    builder.setIcon(R.drawable.user)
                    builder.setPositiveButton("Yes") { _, _ ->
                        //clear share preference
                        loginSharePreference = LoginSharePreference(this)
                        loginSharePreference.clearLogin()
                        Intent(this, LoginActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }
                    builder.setNegativeButton("No") { _, _ ->
                    }
                    builder.show()


                }

            }

            fragmentTransaction.commit()
            invalidateOptionsMenu()

            binding.drawerLayout.closeDrawer(GravityCompat.START)

            true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return true
    }

    private fun updateUserInformation() {
        val username = loginSharePreference.getID()
        val fullname = loginSharePreference.getName()
        val role = loginSharePreference.getRole()

        val headerView = binding.navView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.user_name)
        val userFullNameTextView = headerView.findViewById<TextView>(R.id.user_full_name)
        val userRole = headerView.findViewById<TextView>(R.id.txt_role)

        userNameTextView.text = "@$username"
        userFullNameTextView.text = fullname
        userRole.text = role
    }

    override fun onResume() {
        super.onResume()
        updateUserInformation()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search..."
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {


            override fun onQueryTextSubmit(query: String): Boolean {
                sharedViewModel.searchText.value = query

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                sharedViewModel.searchText.value = newText

                return false
            }
        })

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        when (currentFragment) {
            is ManagerBillsFragment, is ManagerBooksFragment, is ManagerCategoryBooksFragment,
            is ManagerMembersFragment, is Top10Fragment -> {
                menu?.findItem(R.id.action_search)?.isVisible = true
            }

            else -> {
                menu?.findItem(R.id.action_search)?.isVisible = false
            }
        }
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (!loginSharePreference.getRemember()) {
            loginSharePreference.clearLogin()
        }
    }

}