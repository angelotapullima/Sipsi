package com.bufeotec.sipcsi.PuebloOpina.MisPublicaciones.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.bufeotec.sipcsi.PuebloOpina.MisPublicaciones.Models.ModelMyFeed;

import java.util.List;

//import com.andr.mvvm.RetrofitRoom.Models.ModelFeed;

public class MyFeedRoomDBRepository {
    private MyFeedInfoDao myFeedInfoDao;
    LiveData<List<ModelMyFeed>> mAllPosts;

    public MyFeedRoomDBRepository(Application application){
         MyFeedRoomDataBase db = MyFeedRoomDataBase.getDatabase(application);
        myFeedInfoDao = db.postInfoDao();
        mAllPosts = myFeedInfoDao.getAllPosts();
     }

    public LiveData<List<ModelMyFeed>> getAllPosts() {
        return mAllPosts;
    }

    public void insertPosts (List<ModelMyFeed> modelMyFeed) {
        new insertAsyncTask(myFeedInfoDao).execute(modelMyFeed);
    }

    public void deleteAllPost() {
        new DeleteAllPostAsyncTask(myFeedInfoDao).execute();
    }


    public void deleteOneFeed(String dato) {
        new DeleteOneMyFeedAsyncTask(myFeedInfoDao).execute(dato);
    }

    public void deleteMovie(ModelMyFeed modelMyFeed) {
        new DeleteMovieAsyncTask(myFeedInfoDao).execute(modelMyFeed);
    }

    private static class insertAsyncTask extends AsyncTask<List<ModelMyFeed>, Void, Void> {

        private MyFeedInfoDao mAsyncTaskDao;

        insertAsyncTask(MyFeedInfoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final List<ModelMyFeed>... params) {
            mAsyncTaskDao.insertPosts(params[0]);
            return null;
        }
    }


    private static class DeleteMovieAsyncTask extends AsyncTask<ModelMyFeed, Void, Void> {
        private MyFeedInfoDao myFeedInfoDao;

        private DeleteMovieAsyncTask(MyFeedInfoDao myFeedInfoDao) {
            this.myFeedInfoDao = myFeedInfoDao;
        }

        @Override
        protected Void doInBackground(ModelMyFeed... params) {
            myFeedInfoDao.deleteMovie(params[0]);
            return null;
        }
    }

    private static class DeleteAllPostAsyncTask extends AsyncTask<Void, Void, Void> {
        private MyFeedInfoDao myFeedInfoDao;

        private DeleteAllPostAsyncTask(MyFeedInfoDao myFeedInfoDao) {
            this.myFeedInfoDao = myFeedInfoDao ;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            myFeedInfoDao.deleteAll();
            return null;
        }
    }

    private static class DeleteOneMyFeedAsyncTask extends AsyncTask<String , Void, Void> {
        private MyFeedInfoDao feedDao;

        private DeleteOneMyFeedAsyncTask(MyFeedInfoDao feedDao) {
            this.feedDao = feedDao;
        }

        @Override
        protected Void doInBackground(String... modelFeeds) {
            feedDao.deleteOneMYFeed(modelFeeds[0]);

            return null;
        }
    }
}
