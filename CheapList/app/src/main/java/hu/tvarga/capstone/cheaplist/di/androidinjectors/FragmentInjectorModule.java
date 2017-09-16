package hu.tvarga.capstone.cheaplist.di.androidinjectors;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import hu.tvarga.capstone.cheaplist.di.scopes.FragmentScope;
import hu.tvarga.capstone.cheaplist.ui.compare.CompareFragment;
import hu.tvarga.capstone.cheaplist.ui.detail.DetailFragment;

@Module
public interface FragmentInjectorModule {

	@FragmentScope
	@ContributesAndroidInjector
	CompareFragment contributesCompareFragmentInjector();

	@FragmentScope
	@ContributesAndroidInjector
	DetailFragment contributesDetailFragmentInjector();
}
