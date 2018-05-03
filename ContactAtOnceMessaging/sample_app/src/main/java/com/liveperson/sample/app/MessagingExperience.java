package com.liveperson.sample.app;

/**
 * Created by abratcher on 2/28/18.
 */

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.contactatonce.messaging.IMessagingDelegate;
import com.contactatonce.messaging.IMessagingNotificationDelegate;
import com.contactatonce.messaging.InitMessagingCallBack;
import com.contactatonce.messaging.Messaging;
import com.contactatonce.messaging.MessagingConfig;
import com.contactatonce.messaging.MessagingConsumerProperties;
import com.contactatonce.messaging.MessagingNotification;
import com.contactatonce.messaging.infra.utils.BrandingWrapperUtil;
import com.liveperson.sample.app.Activities.FragmentContainerActivity;
import com.liveperson.sample.app.Utils.SampleAppStorage;
import com.liveperson.sample.app.push.NotificationUI;
import com.liveperson.sample.app.push.fcm.FirebaseRegistrationIntentService;

import org.jetbrains.annotations.NotNull;

public class MessagingExperience implements IMessagingDelegate, IMessagingNotificationDelegate {
	private static MessagingExperience instance;
	private Boolean messagingInitialized = false;
	private SampleAppStorage storage;
	private Context context;

	private void MessagingExperience() { }

	public static MessagingExperience getInstance() {
		if (instance == null) {
			instance = new MessagingExperience();
		}

		return instance;
	}

	public void initialize(final Context context) {
		this.context = context;
		if (storage == null) {
			storage = SampleAppStorage.getInstance(context);
		}

		MessagingConsumerProperties properties = new MessagingConsumerProperties(
				storage.getCaoApplicationId(), storage.getCAOProviderId(), storage.getCaoBaseDomain()
		);

		Messaging.initialize(context, properties,this, new InitMessagingCallBack() {
					@Override
					public void onInitSucceed() {
						Intent intent = new Intent(context, FirebaseRegistrationIntentService.class);
						context.startService(intent);
						messagingInitialized = true;
						customizeMessaging();
					}

					@Override
					public void onInitFailed(Exception e) {
						new Handler(Looper.getMainLooper()).post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(context, "Init Failed", Toast.LENGTH_SHORT).show();
							}
						});

					}
				}
		);
	}

	public void showConversation(String referenceID, String name, String consumerMessage) {
		if (!messagingInitialized) return;

		Boolean useFullScreen = SampleAppStorage.getInstance(context).getUseFullScreen();

		if (useFullScreen) {
			Messaging.showConversation(context, referenceID, name,consumerMessage);
		} else {
			Intent intent = new Intent(context, FragmentContainerActivity.class);
			intent.putExtra("referenceID", referenceID);
			intent.putExtra("name", name);
			intent.putExtra("consumer_message", consumerMessage);
			context.startActivity(intent);
		}
	}

	public void showAllConversations() {
		Messaging.showAllConversations(context);
	}

	public void handlePush(Intent intent) {
		boolean isFromPush = intent.getBooleanExtra(NotificationUI.PUSH_NOTIFICATION, false);
		if (!isFromPush) {
			return;
		}

		clearPushNotifications();
		String referenceID = intent.getStringExtra(NotificationUI.REFERENCE_ID);
		showConversation(referenceID, "","");
	}

	private void customizeMessaging() {
		BrandingWrapperUtil branding = BrandingWrapperUtil.getInstance();

		branding.put(MessagingConfig.IBrandingConfigurationKeys.CONSUMER_BUBBLE_BACKGROUND_COLOR, "#FFD1A6");
		branding.put(MessagingConfig.IBrandingConfigurationKeys.UNREAD_INDICATOR_BUBBLE_BACKGROUND_COLOR,"#FF9300");
		branding.put(MessagingConfig.IBrandingConfigurationKeys.ALL_CONVERSATIONS_TITLE,"Dealer Messages");
	}

	/**
	 * Hide any shown notification
	 */
	private void clearPushNotifications() {
		((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(NotificationUI.NOTIFICATION_ID);
	}

	public Bitmap noConversationsImage(int targetWidth, int targetHeight) {
		Boolean provideImage = SampleAppStorage.getInstance(context).getProvideNoConversationImage();

		if (provideImage) {
			return BitmapFactory.decodeResource(context.getResources(), R.drawable.no_conversations);
		} else {
			return null;
		}
	}

	@Override
	public boolean shouldShowMessagingNotification(@NotNull MessagingNotification notification) {
		return false;
	}

	@Override
	public void onMessagingNotificationReceived(@NotNull MessagingNotification notification) {
		NotificationUI.showNotification(context, notification);
	}
}
