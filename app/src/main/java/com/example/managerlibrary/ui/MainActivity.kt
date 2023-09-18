package com.example.managerlibrary.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
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

class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private lateinit var loginSharePreference: LoginSharePreference
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

            val fragmentTransaction = fragmentManager.beginTransaction()


            val defaultFragment = ManagerBillsFragment()
            fragmentTransaction.replace(R.id.nav_host_fragment, defaultFragment)
            supportActionBar?.title = resources.getString(R.string.manager_bill)

            fragmentTransaction.commit()
        }
        binding.navView.setNavigationItemSelectedListener { menuItem ->

            val fragmentManager = supportFragmentManager

            val fragmentTransaction = fragmentManager.beginTransaction()

            when (menuItem.itemId) {

                //Manager Bill
                R.id.manager_bill -> {
                    val fragment = ManagerBillsFragment()

                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    supportActionBar?.title = resources.getString(R.string.manager_bill)
                    Toast.makeText(this, "Manager Bill", Toast.LENGTH_SHORT).show()
                }

                //Manager Book
                R.id.manager_book -> {
                    val fragment = ManagerBooksFragment()

                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    supportActionBar?.title = resources.getString(R.string.manager_book)
                    Toast.makeText(this, "Manager Book", Toast.LENGTH_SHORT).show()
                }

                //Manager User
                R.id.manager_user -> {
                    val fragment = ManagerMembersFragment()

                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    supportActionBar?.title = resources.getString(R.string.manager_user)
                    Toast.makeText(this, "Manager User", Toast.LENGTH_SHORT).show()
                }

                //Manager Category Book
                R.id.manager_category -> {
                    val fragment = ManagerCategoryBooksFragment()

                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    supportActionBar?.title = resources.getString(R.string.manager_category_book)
                    Toast.makeText(this, "Manager Category Book", Toast.LENGTH_SHORT).show()
                }

                //Profile
                R.id.user_profile -> {
                    val fragment = ProfileFragment()
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    supportActionBar?.title = resources.getString(R.string.profile)
                    Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                }

                //Change Password
                R.id.change_password -> {
                    val fragment = ChangePasswordFragment()
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    supportActionBar?.title = resources.getString(R.string.change_password)
                    Toast.makeText(this, "Change Password", Toast.LENGTH_SHORT).show()
                }

                //Create new account
                R.id.add_user -> {
                    val fragment = AddNewUserFragment()
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    supportActionBar?.title = resources.getString(R.string.create_new_account)
                    Toast.makeText(this, "Create New Account", Toast.LENGTH_SHORT).show()
                }

                //Top 10 book
                R.id.top10 -> {
                    val fragment = Top10Fragment()
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    supportActionBar?.title = resources.getString(R.string.top_10_book)
                    Toast.makeText(this, "Top 10 Book", Toast.LENGTH_SHORT).show()
                }

                //Revenue
                R.id.money -> {
                    val fragment = RevenueFragment()
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    supportActionBar?.title = resources.getString(R.string.revenue)
                    Toast.makeText(this, "Revenue", Toast.LENGTH_SHORT).show()
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
                        finish()
                        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
                    }
                    builder.setNegativeButton("No") { _, _ ->
                        Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
                    }
                    builder.show()


                }

            }

            fragmentTransaction.commit()

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
}