package com.example.managerlibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
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

class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private lateinit var loginSharePreference: LoginSharePreference
    private lateinit var librarianDAO: LibrarianDAO
    private var doubleBackToExitPressedOnce = false

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


        //get username and user full name by share preference
        loginSharePreference = LoginSharePreference(this)
        val username = loginSharePreference.getID()
        val fullname = loginSharePreference.getName()

        //set username and user full name to navigation header
        val headerView = binding.navView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.user_name)
        val userFullNameTextView = headerView.findViewById<TextView>(R.id.user_full_name)

        // Update the views with your data
        Toast.makeText(this, "Welcome $fullname", Toast.LENGTH_SHORT).show()
        userNameTextView.text = username
        userFullNameTextView.text = fullname


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
                        Intent(this, LoginActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
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

    override fun onBackPressed() {
        val checkSaveLogin = loginSharePreference.getRemember()
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // Close the navigation drawer if it's open
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (doubleBackToExitPressedOnce) {
                // If the back button is pressed twice in quick succession, exit the app
                if (checkSaveLogin == false) {
                    loginSharePreference = LoginSharePreference(this)
                    loginSharePreference.clearLogin()
                    super.onBackPressed()
                }
                return
            }

            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

            // Reset the flag after a short delay (2 seconds) if the back button is not pressed again
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
    }
}