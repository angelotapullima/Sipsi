package com.bufeotec.sipcsi.PuebloOpina.Publicaciones.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

/*import com.andr.mvvm.RetrofitRoom.Models.ModelFeed;
import com.andr.mvvm.RetrofitRoom.Repository.FeedRoomDBRepository;
import com.andr.mvvm.RetrofitRoom.Repository.FeedWebServiceRepository;*/

import com.bufeotec.sipcsi.PuebloOpina.Publicaciones.Models.ModelFeed;
import com.bufeotec.sipcsi.PuebloOpina.Publicaciones.Repository.FeedRoomDBRepository;
import com.bufeotec.sipcsi.PuebloOpina.Publicaciones.Repository.FeedWebServiceRepository;

import java.util.List;

public class FeedListViewModel extends AndroidViewModel {

    private FeedRoomDBRepository feedRoomDBRepository;
    private LiveData<List<ModelFeed>> mAllPosts;
    FeedWebServiceRepository feedWebServiceRepository;
    private final LiveData<List<ModelFeed>> retroObservable;


    public FeedListViewModel(Application application){
        super(application);
        feedRoomDBRepository = new FeedRoomDBRepository(application);
        feedWebServiceRepository = new FeedWebServiceRepository(application);
        retroObservable = feedWebServiceRepository.providesWebService();
        //feedRoomDBRepository.insertPosts(retroObservable.getValue());
        mAllPosts = feedRoomDBRepository.getAllPosts();

    }

    public LiveData<List<ModelFeed>> getAllPosts() {
        return mAllPosts;
    }


    public void deleteAllFeed() {
        feedRoomDBRepository.deleteAllFeed();
    }

    public void deleteOneFeed(String id){
        feedRoomDBRepository.deleteOneFeed(id);
    }
   /*public LiveData<List<ModelFeed>> getProjectRetroListObservable() {
        return retroObservable;
    }*/

/*    public void insert(User user) {
        userRepository.insert(user);
    }*/
}
