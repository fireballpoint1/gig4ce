package com.example.gig4ce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;


import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.SignInUIOptions;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.client.UserStateListener;
import com.amazonaws.mobile.client.results.SignInResult;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.GraphQLResponse;
import com.amplifyframework.api.graphql.MutationType;
import com.amplifyframework.api.graphql.SubscriptionType;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.ResultListener;
import com.amplifyframework.core.StreamListener;
import com.amplifyframework.datastore.generated.model.Task;

import static com.amazonaws.mobile.client.UserState.GUEST;
import static com.amazonaws.mobile.client.UserState.SIGNED_IN;
import static com.amazonaws.mobile.client.UserState.SIGNED_OUT;
import static com.amazonaws.mobile.client.UserState.SIGNED_OUT_FEDERATED_TOKENS_INVALID;
import static com.amazonaws.mobile.client.UserState.SIGNED_OUT_USER_POOLS_TOKENS_INVALID;

public class MainActivity extends AppCompatActivity {

    private Button SendOtp;
    private EditText PhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SendOtp=(Button)findViewById(R.id.btnSendOtp);
        PhoneNumber=(EditText)findViewById(R.id.etPhoneNumber);

        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("AmplifyGetStarted", "Amplify is all setup and ready to go!");
        } catch (AmplifyException exception) {
            Log.e("AmplifyGetStarted", exception.getMessage());
        }

        // initialize https://aws-amplify.github.io/docs/android/authentication#initialization
        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

                    @Override
                    public void onResult(UserStateDetails userStateDetails) {
                        Log.i("INIT", "onResult: " + userStateDetails.getUserState());
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("INIT", "Initialization error.", e);
                    }
                }
        );

         // drop-in auth (second option in documentation)
        // 'this' refers the the current active activity
        AWSMobileClient.getInstance().showSignIn(this, new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails result) {
                Log.d("SIGNIN1", "onResult: " + result.getUserState());
            }

            @Override
            public void onError(Exception e) {
                Log.e("SIGNIN1", "onError: ", e);
            }
        });

//        AWSMobileClient.getInstance().showSignIn(
//                this,
//                SignInUIOptions.builder()
//                        .nextActivity(MainActivity.class) //edited this
//                        .build(),
//                new Callback<UserStateDetails>() {
//                    @Override
//                    public void onResult(UserStateDetails result) {
//                        Log.d("SIGNIN", "onResult: " + result.getUserState());
//                        switch (result.getUserState()){
//                            case SIGNED_IN:
//                                Log.i("INIT", "logged in!");
//                                break;
//                            case SIGNED_OUT:
//                                Log.i("SIGNIN", "onResult: User did not choose to sign-in");
//                                break;
//                            default:
//                                AWSMobileClient.getInstance().signOut();
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        Log.e("SIGNIN", "onError: ", e);
//                    }
//                }
//        );




        // cognito user pool tokens
        AWSMobileClient.getInstance().addUserStateListener(new UserStateListener() {
            @Override
            public void onUserStateChanged(UserStateDetails userStateDetails) {
                switch (userStateDetails.getUserState()){
                    case GUEST:
                        Log.i("userState", "user is in guest mode");
                        break;
                    case SIGNED_OUT:
                        Log.i("userState", "user is signed out");
                        break;
                    case SIGNED_IN:
                        Log.i("userState", "user is signed in");
                        break;
                    case SIGNED_OUT_USER_POOLS_TOKENS_INVALID:
                        Log.i("userState", "need to login again");
                        break;
                    case SIGNED_OUT_FEDERATED_TOKENS_INVALID:
                        Log.i("userState", "user logged in via federation, but currently needs new tokens");
                        break;
                    default:
                        Log.e("userState", "unsupported");
                }
            }
        });


        // add data
//        Task task = Task.builder().title("My first task").description("Get started with Amplify").build();
//
//        Amplify.API.mutate(task, MutationType.CREATE, new ResultListener<GraphQLResponse<Task>>() {
//            @Override
//            public void onResult(GraphQLResponse<Task> taskGraphQLResponse) {
//                Log.i("AmplifyGetStarted", "Added task with id: " + taskGraphQLResponse.getData().getId());
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                Log.e("AmplifyGetStarted", throwable.toString());
//            }
//        });
//
//        // query data
//        Amplify.API.query(Task.class, new ResultListener<GraphQLResponse<Iterable<Task>>>() {
//            @Override
//            public void onResult(GraphQLResponse<Iterable<Task>> iterableGraphQLResponse) {
//                for(Task task : iterableGraphQLResponse.getData()) {
//                    Log.i("AmplifyGetStarted", "Task : " + task.getTitle());
//                }
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                Log.e("AmplifyGetStarted", throwable.toString());
//            }
//        });
//
//        //
//        Amplify.API.subscribe(
//                Task.class,
//                SubscriptionType.ON_CREATE,
//                new StreamListener<GraphQLResponse<Task>>() {
//                    @Override
//                    public void onNext(GraphQLResponse<Task> taskGraphQLResponse) {
//                        Log.i("AmplifyGetStarted", "Subscription detected a create: " +
//                                taskGraphQLResponse.getData().getTitle());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        System.out.println("modi: Completed");
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        Log.e("AmplifyGetStarted", throwable.toString());
//                    }
//                }
//        );
    }

//    protected void onValidOTP(String username,String password,String null,String null){
//        AWSMobileClient.getInstance().addUserStateListener(new UserStateListener() {
//            @Override
//            public void onUserStateChanged(UserStateDetails userStateDetails) {
//                switch (userStateDetails.getUserState()){
//                    case SIGNED_OUT:
//                        // user clicked signout button and signedout
//                        Log.i("userState", "user signed out");
//                        break;
//                    case SIGNED_OUT_USER_POOLS_TOKENS_INVALID:
//                        Log.i("userState", "need to login again.");
//                        AWSMobileClient.getInstance().signIn(username, password, null, new Callback<SignInResult>() {
//                            //...
//                        });
//                        //Alternatively call .showSignIn()
//                        break;
//                    default:
//                        Log.i("userState", "unsupported");
//                }
//            }
//        });
//    }




}
