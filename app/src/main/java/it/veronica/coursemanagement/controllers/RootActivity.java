package it.veronica.coursemanagement.controllers;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.coursemanagement.R;

import it.veronica.coursemanagement.controllers.fragment.ApplicationSettingFragment;
import it.veronica.coursemanagement.controllers.fragment.CatalogueFragment;
import it.veronica.coursemanagement.controllers.fragment.CourseFragment;
import it.veronica.coursemanagement.controllers.fragment.DashboardFragment;
import it.veronica.coursemanagement.controllers.fragment.DisponibilityFragment;
import it.veronica.coursemanagement.controllers.fragment.LoginFragment;
import it.veronica.coursemanagement.controllers.fragment.LogoutFragment;
import it.veronica.coursemanagement.controllers.fragment.ProfileFragment;
import it.veronica.coursemanagement.controllers.fragment.TeacherFragment;
import it.veronica.coursemanagement.controllers.fragment.UserFragment;
import it.veronica.coursemanagement.model.User_type;
import it.veronica.coursemanagement.utility.PreferencesManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RootActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Context myContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myContext = this;
        ManageExtra();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //Verifico il tipo di utenza
        PreferencesManager preferencesManager = PreferencesManager.getInstance(getResources().getString(R.string.preferencesManager), this);
        String user_type_id = preferencesManager.GetPreferenceByKey(getResources().getString(R.string.user_type_id));
        FloatingActionButton menu_type = (navigationView.getHeaderView(0)).findViewById(R.id.menu_type);
        if (user_type_id == null) {user_type_id = "0";}
        navigationView.getMenu().clear();
        if (Integer.parseInt(user_type_id) == User_type.STUDENT.getValue()) {
            navigationView.inflateMenu(R.menu.student_menu);
            menu_type.setImageResource(R.drawable.ic_customer);
        } else if (Integer.parseInt(user_type_id) == User_type.TEACHER.getValue()) {
            navigationView.inflateMenu(R.menu.teacher_menu);
            menu_type.setImageResource(R.drawable.ic_coaching);
        } else if (Integer.parseInt(user_type_id) == User_type.ADMIN.getValue()) {
            navigationView.inflateMenu(R.menu.admin_menu);
            menu_type.setImageResource(R.drawable.ic_admin);
        } else {
            navigationView.inflateMenu(R.menu.guest_menu);
            menu_type.setImageResource(R.drawable.ic_guest);
        }
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dashboard /*, R.id.nav_gallery, R.id.nav_slideshow*/)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
    }

    private void ManageExtra()
    {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        else
        {
            Boolean log_out = extras.getBoolean(getResources().getString(R.string.logged_out));
            if (log_out) {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.logout_done), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            if (id == R.id.catalogue) {
                //Cambio fragment per il catalogo
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )
                        .replace(R.id.nav_host_fragment, CatalogueFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(CatalogueFragment.class.getName()) // name can be null
                        .commit();
            }
            else if (id == R.id.teacher) {
                //Cambio fragment per la lista docenti
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )
                        .replace(R.id.nav_host_fragment, TeacherFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(TeacherFragment.class.getName()) // name can be null
                        .commit();
            }
            else if (id == R.id.disponibility) {
                //Cambio fragment per la lista docenti
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )
                        .replace(R.id.nav_host_fragment, DisponibilityFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(DisponibilityFragment.class.getName()) // name can be null
                        .commit();
            }
            else if (id == R.id.course) {
                //Cambio fragment per la lista corsi
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )
                        .replace(R.id.nav_host_fragment, CourseFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(CourseFragment.class.getName()) // name can be null
                        .commit();
            }
            else if (id == R.id.user) {
                //Cambio fragment per la lista utenti
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )
                        .replace(R.id.nav_host_fragment, UserFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(UserFragment.class.getName()) // name can be null
                        .commit();
            }
            else if (id == R.id.dashboard) {
                //Cambio fragment per la dashboard
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )
                        .replace(R.id.nav_host_fragment, DashboardFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(DashboardFragment.class.getName()) // name can be null
                        .commit();
            }
            else if (id == R.id.profile_menu) {
                //Cambio fragment per il profilo
                PreferencesManager preferencesManager = PreferencesManager.getInstance(getResources().getString(R.string.preferencesManager), myContext);
                Fragment profileFragment = new ProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(getResources().getString(R.string.user_id), Integer.parseInt(preferencesManager.GetPreferenceByKey(getResources().getString(R.string.user_id))));
                profileFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )
                        .replace(R.id.nav_host_fragment, profileFragment, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(ProfileFragment.class.getName()) // name can be null
                        .commit();
            }
            else if (id == R.id.setting) {
                //Cambio fragment per il login
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )
                        .replace(R.id.nav_host_fragment, ApplicationSettingFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(ApplicationSettingFragment.class.getName()) // name can be null
                        .commit();
            }
            else if (id == R.id.login) {
                //Cambio fragment per il login
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )
                        .replace(R.id.nav_host_fragment, LoginFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(LoginFragment.class.getName()) // name can be null
                        .commit();
            }
            else if(id == R.id.logout){
                //Cambio fragment per il logout
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )
                        .replace(R.id.nav_host_fragment, LogoutFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(LogoutFragment.class.getName()) // name can be null
                        .commit();
            }
            return true;
        }
    };
}