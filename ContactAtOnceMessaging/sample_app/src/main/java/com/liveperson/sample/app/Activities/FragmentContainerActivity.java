package com.liveperson.sample.app.Activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.contactatonce.messaging.Messaging;
import com.liveperson.sample.app.R;


/**
 * ***** Sample app class - Not related to MessagingController SDK ****
 *
 * Used as an example of how to use SDK "Fragment mode"
 */
public class FragmentContainerActivity extends AppCompatActivity {

    private static final String TAG = FragmentContainerActivity.class.getSimpleName();
    private static final String LIVEPERSON_FRAGMENT = "liveperson_fragment";
    private Fragment mConversationFragment;
    private String referenceID;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        Log.i(TAG, "onCreate savedInstanceState = " + savedInstanceState );
        referenceID = getIntent().getStringExtra("referenceID");
        name = getIntent().getStringExtra("name");
        final String consumerMessage = getIntent().getStringExtra("consumer_message");
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				initFragment(referenceID, name,consumerMessage);
			}
		});
    }

    private void initFragment(String referenceID, String name, String consumerMessage) {
        mConversationFragment = getSupportFragmentManager().findFragmentByTag(LIVEPERSON_FRAGMENT);
        Log.d(TAG, "initFragment. mConversationFragment = "+ mConversationFragment);
        if (mConversationFragment == null) {
            mConversationFragment = Messaging.getConversationFragment(referenceID, name,consumerMessage);

            if (isValidState()) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.custom_fragment_container, mConversationFragment, LIVEPERSON_FRAGMENT).commitAllowingStateLoss();
            }
        }else{
             attachFragment();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private boolean isValidState() {
        return !isFinishing() && !isDestroyed();
    }

    private void attachFragment() {
        if (mConversationFragment.isDetached()) {
            Log.d(TAG, "initFragment. attaching fragment");
            if (isValidState()){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.attach(mConversationFragment).commitAllowingStateLoss();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mConversationFragment != null){
            attachFragment();
        }
    }
}
