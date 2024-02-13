package com.bannet.skils.service;

import com.bannet.skils.addskilss.responce.SkillList;
import com.bannet.skils.post.responce.PostinglistModel;
import com.bannet.skils.professinoldetails.responce.ProfessionalList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterfacepagi {


    @POST("get_skills")
    @FormUrlEncoded
    Call<SkillList> getSkills(@Field("language") String language,@Field("page_no")String page_no);

    @POST("posting_list")  // posting_list
    @FormUrlEncoded
    Call<PostinglistModel> postingslist(@Field("latitude") String latitude,
                                        @Field("longitude") String longitude,
                                        @Field("distnace_in_miles") String distnace_in_miles,
                                        @Field("search_key") String search_key,
                                        @Field("language") String language,
                                        @Field("page_no") String page_no);

    @POST("professional_list")
    @FormUrlEncoded
    Call<ProfessionalList> professionallist(@Field("latitude") String latitude,
                                            @Field("longitude") String longitude,
                                            @Field("distnace_in_miles") String distnace_in_miles,
                                            @Field("search_key") String search_key,
                                            @Field("language") String language,
                                            @Field("page_no") String page_no);

    @POST("professional_list_category")
    @FormUrlEncoded
    Call<ProfessionalList> catProff(@Field("latitude") String latitude,
                                    @Field("longitude") String longitude,
                                    @Field("language") String language,
                                    @Field("category_id") String category_id,
                                    @Field("skill_id") String skill_id,
                                    @Field("search_key") String search_key,
                                    @Field("page_no") String page_no);

    @POST("posting_list_category")
    @FormUrlEncoded
    Call<PostinglistModel> catPosting(@Field("latitude") String latitude,
                                      @Field("longitude") String longitude,
                                      @Field("language") String language,
                                      @Field("category_id") String category_id,
                                      @Field("skill_id") String skill_id,
                                      @Field("search_key") String search_key,
                                      @Field("page_no") String page_no);

}
