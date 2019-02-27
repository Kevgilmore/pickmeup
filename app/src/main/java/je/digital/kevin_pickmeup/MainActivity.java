package je.digital.kevin_pickmeup;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new PassengerFragment(),"A").commit();
    }

    // this handles the bottom navigation so when you click an item it changes fragment
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment passengerFragment = getSupportFragmentManager().findFragmentByTag("A");
                    Fragment driverFragment = getSupportFragmentManager().findFragmentByTag("B");

                    switch (menuItem.getItemId()) {
                        case R.id.nav_passenger:
                            if(passengerFragment.isHidden()) {
                                getSupportFragmentManager().beginTransaction().show(passengerFragment).commit();
                                getSupportFragmentManager().beginTransaction().hide(driverFragment).commit();
                            }
                            break;

                        case R.id.nav_driver:
                            if(driverFragment==null) {
                                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new DriverFragment(),"B").commit();
                            }
                            else {
                                if(driverFragment.isHidden()) {
                                    getSupportFragmentManager().beginTransaction().show(driverFragment).commit();
                                }
                            }
                            getSupportFragmentManager().beginTransaction().hide(passengerFragment).commit();
                            break;
                    }
                    return true;
                }
    };
}
