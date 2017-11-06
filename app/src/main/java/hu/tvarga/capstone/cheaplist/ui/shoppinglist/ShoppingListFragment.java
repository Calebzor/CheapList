package hu.tvarga.capstone.cheaplist.ui.shoppinglist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import hu.tvarga.capstone.cheaplist.R;
import hu.tvarga.capstone.cheaplist.business.compare.shoppinglist.ShoppingListContract;
import hu.tvarga.capstone.cheaplist.dao.ShoppingListItem;
import hu.tvarga.capstone.cheaplist.ui.MainActivity;

public class ShoppingListFragment extends DaggerFragment implements ShoppingListContract.View {

	public static final String FRAGMENT_TAG = ShoppingListFragment.class.getName();

	public static ShoppingListFragment newInstance() {
		return new ShoppingListFragment();
	}

	@BindView(R.id.emptyText)
	TextView emptyText;

	@BindView(R.id.shoppingList)
	RecyclerView shoppingList;

	@Inject
	ShoppingListContract.Presenter presenter;

	private Unbinder unbinder;

	@Override
	public void onResume() {
		super.onResume();
		presenter.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		presenter.onPause();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.shopping_list, container, false);
		unbinder = ButterKnife.bind(this, rootView);
		return rootView;
	}

	//region Swipe action
	private void setUpSwipeAction() {
		ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
				0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

			@Override
			public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
					RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
				if (viewHolder instanceof ShoppingListItemHolder) {
					ShoppingListItemHolder shoppingListItemHolder =
							(ShoppingListItemHolder) viewHolder;
					if (shoppingListItemHolder.item != null) {
						presenter.removeFromList(shoppingListItemHolder.item);
						View coordinatorLayout = getActivity().findViewById(R.id.coordinator);
						if (coordinatorLayout != null) {
							showSnackBar(coordinatorLayout, shoppingListItemHolder.item);
						}
					}
				}
			}
		};

		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
		itemTouchHelper.attachToRecyclerView(shoppingList);
	}

	private void showSnackBar(View coordinatorLayout, final ShoppingListItem item) {
		Snackbar.make(coordinatorLayout, R.string.removed_from_shopping_list, Snackbar.LENGTH_LONG)
				.setAction(R.string.undo, new View.OnClickListener() {
					@Override
					public void onClick(View undoView) {
						snackUndoAction(item);
					}
				}).setActionTextColor(
				ContextCompat.getColor(coordinatorLayout.getContext(), R.color.secondaryTextColor))
				.show();
	}

	private void snackUndoAction(ShoppingListItem item) {
		presenter.addToList(item);
	}
	//endregion

	@Override
	public void setEmptyView(int itemCount) {
		shoppingList.setVisibility(itemCount != 0 ? View.VISIBLE : View.GONE);
		emptyText.setVisibility(itemCount == 0 ? View.VISIBLE : View.GONE);
	}

	@Override
	public View.OnClickListener getOnListItemOnClickListener(final ShoppingListItem item,
			final ShoppingListItemHolder holder) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				((MainActivity) getActivity()).openDetailView(item, holder);
			}
		};
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		RecyclerView.Adapter<ShoppingListItemHolder> adapter = presenter.getAdapter();
		shoppingList.setAdapter(adapter);
		setUpSwipeAction();
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

}
