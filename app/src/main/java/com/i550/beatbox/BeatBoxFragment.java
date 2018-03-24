package com.i550.beatbox;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.i550.beatbox.databinding.FragmentBeatBoxBinding;
import com.i550.beatbox.databinding.ListItemSoundBinding;

import java.util.List;


public class BeatBoxFragment extends Fragment {
    private BeatBox mBeatBox; // создаем экземпляр
    public static BeatBoxFragment newInstance() {
       return new BeatBoxFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);    //сохраняет состояние фрагмента между изменениями конфига!!!
        mBeatBox = new BeatBox(getActivity());
    }
    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentBeatBoxBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_beat_box,container,false);
//задуваем макет
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3)); //назначаем грид с 3 столбцами
        binding.recyclerView.setAdapter(new SoundAdapter(mBeatBox.getSounds()));
        return binding.getRoot();
    }

//________________________________________________________________________


    private class SoundHolder extends RecyclerView.ViewHolder{
        private ListItemSoundBinding mBinding;
        private SoundHolder(ListItemSoundBinding binding){
            super(binding.getRoot());
            mBinding=binding;
            mBinding.setViewModel(new SoundViewModel(mBeatBox));
        }   //присоединяем к классу привязки нью объект СВМодел
        //подключаем ВьюМодел

        public void bind(Sound sound){
            mBinding.getViewModel().setSound(sound);//обновляется данные
            mBinding.executePendingBindings();  //немедленно
        }
    }

//________________________________________________________________________


    private class SoundAdapter extends RecyclerView.Adapter<SoundHolder>{
        private List<Sound> mSounds;

        public SoundAdapter(List<Sound> sounds){
            mSounds=sounds;
        }

        @Override public SoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            ListItemSoundBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item_sound,parent,false);
            return new SoundHolder(binding);
        }

        @Override public void onBindViewHolder(SoundHolder holder, int position) {
            Sound sound = mSounds.get(position);
            holder.bind(sound);
        }

        @Override public int getItemCount() {return mSounds.size();}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBeatBox.release();
    }
}
