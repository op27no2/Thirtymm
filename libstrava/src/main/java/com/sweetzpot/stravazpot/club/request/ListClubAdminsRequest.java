package com.sweetzpot.stravazpot.club.request;

import com.sweetzpot.stravazpot.athlete.model.Athlete;
import com.sweetzpot.stravazpot.club.api.ClubAPI;
import com.sweetzpot.stravazpot.club.rest.ClubRest;

import java.util.List;

import retrofit2.Call;

public class ListClubAdminsRequest {

    private final long clubID;
    private final ClubRest restService;
    private final ClubAPI api;
    private Integer page;
    private Integer perPage;

    public ListClubAdminsRequest(long clubID, ClubRest restService, ClubAPI api) {
        this.clubID = clubID;
        this.restService = restService;
        this.api = api;
    }

    public ListClubAdminsRequest inPage(int page) {
        this.page = page;
        return this;
    }

    public ListClubAdminsRequest perPage(int perPage) {
        this.perPage = perPage;
        return this;
    }

    public List<Athlete> execute() {
        Call<List<Athlete>> call = restService.getClubAdmins(clubID, page, perPage);
        return api.execute(call);
    }
}
