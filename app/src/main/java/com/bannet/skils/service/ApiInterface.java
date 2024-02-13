package com.bannet.skils.service;

import com.bannet.skils.Model.AdsCoverageList;
import com.bannet.skils.coupon.response.CheckPrmocodeResponse;
import com.bannet.skils.coupon.response.modelReward;
import com.bannet.skils.explore.response.CategoryResponce;
import com.bannet.skils.profile.responce.DeleteProfile;
import com.bannet.skils.Model.EditskillsModel;
import com.bannet.skils.Model.ForgotPasswordModel;
import com.bannet.skils.Model.PlanStatusModel;
import com.bannet.skils.professinoldetails.responce.ProfessionalList;
import com.bannet.skils.chat.responce.SentMessageModel;
import com.bannet.skils.post.responce.AddPostModel;
import com.bannet.skils.Model.WebModel.AdvertismentModel;
import com.bannet.skils.profile.responce.CertificateList;
import com.bannet.skils.changepassword.responce.ChangepasswordModel;
import com.bannet.skils.chat.responce.ChatListModel;
import com.bannet.skils.profile.responce.CityModel;
import com.bannet.skils.Model.WebModel.CommonModel;
import com.bannet.skils.profile.responce.CountryModel;
import com.bannet.skils.post.responce.EditPostModel;
import com.bannet.skils.profile.responce.EditProfileModel;
import com.bannet.skils.favorite.responce.FavouriteListModel;
import com.bannet.skils.favposting.responce.FavpostingsListModel;
import com.bannet.skils.mobilenumberverification.responce.GetOtpModel;
import com.bannet.skils.profile.responce.ImageUpload;
import com.bannet.skils.login.responce.LoginModel;
import com.bannet.skils.myposting.responce.MypostingListModel;
import com.bannet.skils.notification.responce.NotificationModel;
import com.bannet.skils.otpverification.responce.OtpVerificationModel;
import com.bannet.skils.post.responce.PostinglistModel;
import com.bannet.skils.addskilss.responce.ProfileUpload;
import com.bannet.skils.postingdetails.responce.RatingForPostModel;
import com.bannet.skils.language.responce.SelectLanguageModel;
import com.bannet.skils.addskilss.responce.SkillList;
import com.bannet.skils.profile.responce.StateModel;
import com.bannet.skils.myposting.responce.deletePostModel;
import com.bannet.skils.profile.responce.getprofileModel;
import com.bannet.skils.postingdetails.responce.postingDetailsModel;
import com.bannet.skils.professinoldetails.responce.professionalDetailsModel;
import com.bannet.skils.home.responce.profileimagestatusModel;
import com.bannet.skils.subcategoryhome.responce.NewSubcategoryResponse;
import com.bannet.skils.subcategoryhome.responce.SubcategoryResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

//forgotPassword
    @POST("forgotPassword")  //
    @FormUrlEncoded
    Call<ForgotPasswordModel> ForgotPassword(@Field("phone_no") String phone_no,
                                             @Field("language") String language);

//    Mobile Number Verification
    @POST("get_otp")
    @FormUrlEncoded
    Call<GetOtpModel> RegisterMobileNumber(@Field("phone_no") String phone_no,
                                           @Field("language") String language);


//    OTP verification
    @POST("confirm_otp")
    @FormUrlEncoded
    Call<OtpVerificationModel> otpVerificationSignup(@Field("id") String user_id,
                                                     @Field("otp") String user_otp,
                                                     @Field("phone_no") String phone_no,
                                                     @Field("language") String language);

    //    update language
    @POST("update_language")
    @FormUrlEncoded
    Call<CommonModel> updateLanguage(@Field("user_id") String phone_no,
                                     @Field("language") String language);



    //    Select language
    @POST("language_list")
    Call<SelectLanguageModel> LanguageList();


    //    Forget password change   //
    @POST("forgot_change_password")
    @FormUrlEncoded
    Call<CommonModel>  ForgetPasswordChange(@Field("phone_no") String phone_no,
                                            @Field("new_password") String new_password,
                                            @Field("confirm_pasword") String confirm_pasword,
                                            @Field("language") String language);
//   Profile image upload
    @Multipart
    @POST("doUpload")
    Call<ImageUpload> profileImageUpload(@Part MultipartBody.Part file,
                                         @Part("file_type") RequestBody file_type);

    //add_skills  //
    @POST("add_skills")
    @FormUrlEncoded
    Call<CommonModel> addSkills(@Field("skill_name") String skill_name,
                                @Field("about") String about,
                                @Field("skills_image") String skills_image,
                                @Field("user_id") String user_id,
                                @Field("language") String language,
                                @Field("category_id") String category_id);




    //    Profile  upload  //
    @POST("register")
    @FormUrlEncoded
    Call<ProfileUpload> userRegister (@Field("id") String user_id,
                                      @Field("first_name") String first_name,
                                      @Field("last_name") String last_name,
                                      @Field("image_name") String image_name,
                                      @Field("company_name") String company_name,
                                      @Field("country") String country_id,
                                      @Field("state") String state_id,
                                      @Field("city") String city_id,
                                      @Field("country_name") String country_name,
                                      @Field("state_name") String state_name,
                                      @Field("city_name") String city_name,
                                      @Field("about") String about,
                                      @Field("email") String email,
                                      @Field("password") String password,
                                      @Field("certification") String certification,
                                      @Field("device_type") String device_type,
                                      @Field("device_token") String device_token,
                                      @Field("device_model") String device_model,
                                      @Field("device_version") String device_version,
                                      @Field("gender") String gender,
                                      @Field("skill_id") String skill_id,
                                      @Field("available_from") String available_from,
                                      @Field("available_to") String available_to,
                                      @Field("language") String language,
                                      @Field("latitude") String latitude,
                                      @Field("longitude") String longitude,
                                      @Field("address") String address,
                                      @Field("skills_name") String skills_name,
                                      @Field("banner_image") String banner_image,
                                      @Field("ref_code") String ref_code);


//    User login
    @POST("login")
    @FormUrlEncoded
    Call<LoginModel> userLogin (@Field("phone_no") String phone_no,
                                @Field("password") String password,
                                @Field("device_token") String device_token,
                                @Field("device_version") String device_version,
                                @Field("device_type") String device_type,
                                @Field("device_model") String device_model,
                                @Field("language") String language);

    //get country list  //
    @POST("CountryList")
    @FormUrlEncoded
    Call<CountryModel> getCountry(@Field("language") String language);

    //get state list  //
    @POST("state_list")
    @FormUrlEncoded
    Call<StateModel> getState(@Field("country_id") String country_id,
                              @Field("language") String language);


    //get city list   //
    @POST("cities_list")
    @FormUrlEncoded
    Call<CityModel> getCity(@Field("state_id") String state_id,
                            @Field("language") String language);

    //certificate list
    @POST("get_certificate")
    @FormUrlEncoded
    Call<CertificateList> getCertificate(@Field("language") String language);

    //skill list
    @POST("skills_list")
    @FormUrlEncoded
    Call<SkillList> getSkills(@Field("language") String language);

    //professionList  //
    @POST("professional_list")
    @FormUrlEncoded
    Call<ProfessionalList> professionallist(@Field("latitude") String latitude,
                                            @Field("longitude") String longitude,
                                            @Field("distnace_in_miles") String distnace_in_miles,
                                            @Field("search_key") String search_key,
                                            @Field("language") String language);

    @POST("posting_list")  // posting_list
    @FormUrlEncoded
    Call<PostinglistModel> postingslist(@Field("latitude") String latitude,
                                        @Field("longitude") String longitude,
                                        @Field("distnace_in_miles") String distnace_in_miles,
                                        @Field("search_key") String search_key,
                                        @Field("language") String language);

    @POST("add_remove_fav_prof") //
    @FormUrlEncoded
    Call<CommonModel> addOrremoveFavProfessional(@Field("user_id") String user_id,
                                                 @Field("prof_id") String prof_id,
                                                 @Field("language") String language);

    @POST("add_remove_fav_post") //
    @FormUrlEncoded
    Call<CommonModel> addOrremoveFavPost(@Field("user_id") String user_id,
                                         @Field("post_id") String prof_id,
                                         @Field("language") String language);

    @POST("add_rating_prof")  //
    @FormUrlEncoded
    Call<RatingForPostModel> rateProfessional(@Field("user_id") String user_id,
                                              @Field("prof_id") String prof_id,
                                              @Field("rating") Integer rating,
                                              @Field("language") String language
    );

    @POST("fav_postings")   //
    @FormUrlEncoded
    Call<FavpostingsListModel> fav_postingslist(@Field("user_id") String user_id,
                                                @Field("language") String language);

    @POST("fav_professionals") //
    @FormUrlEncoded
    Call<FavouriteListModel> fav_professionalsList(@Field("user_id") String user_id,
                                                   @Field("language") String language);

    @POST("ads_list")    //
    @FormUrlEncoded
    Call<AdvertismentModel> getAdsList(@Field("latitude") String latitude,
                                       @Field("longitude") String  longitude,
                                       @Field("language") String language);

    @POST("my_notifications")   //
    @FormUrlEncoded
    Call<NotificationModel> getNotificationList(@Field("user_id") String user_id,
                                                @Field("language") String language);

    @POST("add_postings")   //
    @FormUrlEncoded
    Call<AddPostModel> addNewPost(@Field("user_id") String user_id,
                                  @Field("title") String title,
                                  @Field("description") String description,
                                  @Field("skills_id") String skills_id,
                                  @Field("country") String country,
                                  @Field("state") String state,
                                  @Field("city") String city,
                                  @Field("post_images") String post_images,
                                  @Field("latitude") String latitude,
                                  @Field("longitude") String longitude,
                                  @Field("skills_name") String skills_name,
                                  @Field("country_id") String country_id,
                                  @Field("state_id") String state_id,
                                  @Field("city_id") String city_id,
                                  @Field("address") String address,
                                  @Field("language") String language);


    @POST("ads_plan_list")  //
    @FormUrlEncoded
     Call<AdsCoverageList> showPlan( @Field("language") String language);


    @POST("EditProfile")  //
    @FormUrlEncoded
    Call<EditProfileModel> geteditprofile(@Field("id") String user_id,
                                          @Field("first_name") String first_name,
                                          @Field("last_name") String last_name,
                                          @Field("image_name") String image_name,
                                          @Field("company_name") String company_name,
                                          @Field("email") String email,
                                          @Field("country") String country,
                                          @Field("state") String state,
                                          @Field("city") String city,
                                          @Field("country_name") String country_name,
                                          @Field("state_name") String state_name,
                                          @Field("city_name") String city_name,
                                          @Field("about") String about,
                                          @Field("gender") String gender,
                                          @Field("longitude") String longitude,
                                          @Field("latitude") String latitude,
                                          @Field("certification") String certification,
                                          @Field("language") String language,
                                          @Field("device_token") String device_token,
                                          @Field("device_type") String device_type,
                                          @Field("device_version") String device_version,
                                          @Field("device_model") String device_model,
                                          @Field("address") String address);


    //my posting list   //
    @POST("my_postings")
    @FormUrlEncoded
    Call<MypostingListModel> mypostingslist(@Field("user_id") String user_id ,
                                            @Field("language") String language);

    //my posting list  //
    @POST("delete_postings")
    @FormUrlEncoded
    Call<deletePostModel> detetingpost(@Field("user_id") String user_id,
                                       @Field("post_id") String post_id,
                                       @Field("language") String language);


    @POST("edit_postings")   //
    @FormUrlEncoded
    Call<EditPostModel> editPost(@Field("user_id") String user_id,
                                 @Field("title") String title,
                                 @Field("description") String description,
                                 @Field("skills_id") String skills_id,
                                 @Field("country") String country,
                                 @Field("state") String state,
                                 @Field("city") String city,
                                 @Field("post_images") String post_images,
                                 @Field("latitude") String latitude,
                                 @Field("longitude") String longitude,
                                 @Field("skills_name") String skills_name,
                                 @Field("post_id") String post_id,
                                 @Field("country_id") String country_id,
                                 @Field("city_id") String city_id,
                                 @Field("state_id") String state_id,
                                 @Field("address") String address,
                                 @Field("language") String language);


    @POST("changePassword")  //
    @FormUrlEncoded
    Call<ChangepasswordModel> getchangepassword (@Field("id") String user_id,
                                                 @Field("old_password") String old_password,
                                                 @Field("new_password") String new_password,
                                                 @Field("confirm_password")String confirm_password,
                                                 @Field("language") String language);


    //my posting details   //
    @POST("posting_details")
    @FormUrlEncoded
    Call<postingDetailsModel> postingDetails(@Field("user_id") String user_id,
                                             @Field("post_id") String post_id,
                                             @Field("language") String language);


    //my professional_details   //
    @POST("professional_details")
    @FormUrlEncoded
    Call<professionalDetailsModel> professionalDetails(@Field("user_id") String user_id,
                                                       @Field("professional_id") String professional_id,
                                                       @Field("language") String language);



    //get profile_details
    @POST("get_profiles")   //
    @FormUrlEncoded
    Call<getprofileModel> getprofile(@Field("user_id") String user_id,
                                     @Field("language") String language);


    //get profile_details
    @POST("get_profile_image_status")   //
    @FormUrlEncoded
    Call<profileimagestatusModel> profileimagestatus(@Field("user_id") String user_id,
                                                     @Field("language") String language);


    @POST("delete_profile")  //
    @FormUrlEncoded
    Call<DeleteProfile> getDeleteprofile(@Field("user_id") String user_id,
                                         @Field("language") String language);

    @POST("EditProfileSkills")  //
    @FormUrlEncoded
    Call<EditskillsModel> editProfileSkils(@Field("id") String user_id,
                                           @Field("skill_id") String skill_id,
                                           @Field("skills_name") String skills_name,
                                           @Field("available_from") String available_from,
                                           @Field("available_to") String available_to,
                                           @Field("banner_image") String banner_img,
                                           @Field("language") String language);


//    @POST("update_plan")
//    @FormUrlEncoded
//    Call<DeleteProfile> updatePlan(@Field("user_id") String user_id,
//                                   @Field("subscriped_status") String subscriped_status);


    @POST("my_chats")   //
    @FormUrlEncoded
    Call<ChatListModel> chatList(@Field("user_id") String user_id,
                                 @Field("language") String language);


    @POST("send_chat_msg")  //
    @FormUrlEncoded
    Call<SentMessageModel> sentMessage(@Field("user_id") String user_id,
                                       @Field("opp_user_id") String opp_user_id,
                                       @Field("chat_id") String chat_id,
                                       @Field("message") String message,
                                       @Field("firebase_id") String firebase_id,
                                       @Field("message_type") String message_type,
                                       @Field("language") String language);


    @POST("my_purchase")  //
    @FormUrlEncoded
    Call<PlanStatusModel> planStatusList(@Field("user_id") String user_id,
                                         @Field("language") String language);

    @POST("notification_delete")  //
    @FormUrlEncoded
    Call<CommonModel> deleteNotification(@Field("user_id") String user_id,
                                         @Field("language") String language);


    @POST("purchase_plan")  //
    @FormUrlEncoded
    Call<CommonModel> purchasePlan(@Field("user_id") String user_id,
                                   @Field("plan_id") String plan_id,
                                   @Field("city_id") String city_id,
                                   @Field("state_id") String state_id,
                                   @Field("country_id") String country_id,
                                   @Field("city_name") String city_name,
                                   @Field("state_name") String state_name,
                                   @Field("country_name") String country_name,
                                   @Field("transaction_id") String transaction_id,
                                   @Field("device") String device,
                                   @Field("plan_days") String plan_days,
                                   @Field("latitude") String latitude,
                                   @Field("longitude") String logitude,
                                   @Field("address") String address,
                                   @Field("language") String language);

    @POST("notification_delete_id")  //
    @FormUrlEncoded
    Call<CommonModel> singleNotificationDelete(@Field("id") String id,
                                               @Field("language") String language);

    @POST("check_user")  //
    @FormUrlEncoded
    Call<CommonModel> CheckUser(@Field("user_id") String user_id);


    @POST("report_block")  //
    @FormUrlEncoded
    Call<CommonModel> report_block_user(@Field("user_id") String user_id,@Field("report_user_id") String report_user_id,
                                          @Field("type") String type,@Field("language") String language);

    @POST("withdraw_request")  //
    @FormUrlEncoded
    Call<CommonModel> withdrawRequest(@Field("user_id") String user_id,
                                      @Field("language") String language,
                                      @Field("payment_id") String payment_id);

    //     categorylist
    @POST("get_category")
    @FormUrlEncoded
    Call<CategoryResponce> categoryList(@Field("language") String language);
    @POST("get_category_list")
    @FormUrlEncoded
    Call<CategoryResponce> categoryListPage(@Field("language") String language,
                                           @Field("page_no") String page_no);



    //     subcategorylist
    @POST("get_category_skilss")
    @FormUrlEncoded
    Call<SubcategoryResponse> subcategoryList(@Field("category_id") String category_id,
                                              @Field("language") String language,
                                              @Field("page_no") String page_no,
                                              @Field("search") String search);

    @POST("get_category_skilss")
    @FormUrlEncoded
    Call<SubcategoryResponse> newsubcategoryList(@Field("user_id") String user_id,
                                                    @Field("category_id") String category_id,
                                                    @Field("language") String language,
                                                    @Field("page_no") String page_no,
                                                    @Field("search") String search);


    @POST("update_device")
    @FormUrlEncoded
    Call<CommonModel> updateDevice(@Field("user_id") String user_id,
                                   @Field("device_type") String device_type,
                                   @Field("device_token") String device_token,
                                   @Field("device_model") String device_model,
                                   @Field("device_version") String device_version,
                                   @Field("language") String language);


    @POST("update_device")
    @FormUrlEncoded
    Call<ProfessionalList> catProff(@Field("latitude") String user_id,
                                    @Field("longitude") String device_type,
                                    @Field("language") String device_token,
                                    @Field("category_id") String device_model,
                                    @Field("skill_id") String device_version,
                                    @Field("search_key") String language,
                                    @Field("page_no") String page_no);

    @POST("update_device")
    @FormUrlEncoded
    Call<ProfessionalList> catPosting(@Field("latitude") String latitude,
                                     @Field("longitude") String device_type,
                                     @Field("language") String device_token,
                                     @Field("category_id") String device_model,
                                     @Field("skill_id") String device_version,
                                     @Field("search_key") String language,
                                     @Field("page_no") String page_no);

    @POST("top_rated_posting")  // posting_list
    @FormUrlEncoded
    Call<PostinglistModel> Toppostingslist(@Field("latitude") String latitude,
                                           @Field("longitude") String longitude,
                                           @Field("language") String language,
                                           @Field("search_key") String search_key);


    @POST("promocode_list")
    @FormUrlEncoded
    Call<modelReward> promocodeList(@Field("language") String language,
                                    @Field("latitude") String latitude,
                                    @Field("longitude") String longitude);


    @POST("check_promocode")
    @FormUrlEncoded
    Call<CheckPrmocodeResponse> checkpromocode(@Field("user_id") String user_id,
                                               @Field("promo_code_id") String promo_code_id,
                                               @Field("language") String language);

    @POST("use_coupon")
    @FormUrlEncoded
    Call<CommonModel> promocodeused(@Field("user_id") String user_id,
                                    @Field("promocode_id") String promocode_id,
                                    @Field("language") String language);



}
