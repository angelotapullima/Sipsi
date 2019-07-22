package com.bufeotec.sipcsi.PuebloOpina.MisPublicaciones.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.bufeotec.sipcsi.PuebloOpina.MisPublicaciones.Models.ModelMyFeed;
import com.bufeotec.sipcsi.PuebloOpina.MisPublicaciones.Repository.MyFeedRoomDBRepository;
import com.bufeotec.sipcsi.PuebloOpina.MisPublicaciones.Repository.MyFeedWebServiceRepository;

import java.util.List;

public class MyFeedListViewModel extends AndroidViewModel {

    private MyFeedRoomDBRepository myFeedRoomDBRepository;
    private LiveData<List<ModelMyFeed>> mAllPosts;
    MyFeedWebServiceRepository myFeedWebServiceRepository;
    private final LiveData<List<ModelMyFeed>> retroObservable;


    public MyFeedListViewModel(Application application){
        super(application);
        myFeedRoomDBRepository = new MyFeedRoomDBRepository(application);
        myFeedWebServiceRepository = new MyFeedWebServiceRepository(application);
        retroObservable = myFeedWebServiceRepository.providesWebService();
        //myFeedRoomDBRepository.insertPosts(retroObservable.getValue());
        mAllPosts = myFeedRoomDBRepository.getAllPosts();

    }

    public LiveData<List<ModelMyFeed>> getAllPosts() {
        return mAllPosts;
    }

   /*public LiveData<List<ModelFeed>> getProjectRetroListObservable() {
        return retroObservable;
    }*/

    public void deleteAllPost() {
        myFeedRoomDBRepository.deleteAllPost();
    }

    public void deleteOneFeed(String id){
        myFeedRoomDBRepository.deleteOneFeed(id);
    }
/*    public void insert(User user) {
        userRepository.insert(user);
    }*/
}
