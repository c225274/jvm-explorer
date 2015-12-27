package com.blogspot.mikelaud.je.ui.search;

import com.blogspot.mikelaud.je.domain.Type;
import com.blogspot.mikelaud.je.ui.background.UiBackground;
import com.google.inject.Inject;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class UiSearchImpl implements UiSearch {

	private final ViewContext CONTEXT;
	private final UiSearchConst CONST;
	private final UiBackground BACKGROUND;
	//
	private final BorderPane PANE;
	private final TextField SEARCH_FIELD;
	
	@Inject
	private UiSearchImpl
	(	ViewContext aContext
	,	UiSearchConst aConst
	,	UiBackground aBackground
	) {
		CONTEXT = aContext;
		CONST = aConst;
		BACKGROUND = aBackground;
		//
		PANE = new BorderPane();
		SEARCH_FIELD = new TextField();
		//
		buildForm();
	}

	private Node createMatching() {
		Label countFoundLabel = new Label();
		Label countLabel = new Label(CONST.getCountLabel());
		Label countAllLabel = new Label();
		//
		countFoundLabel.textProperty().bind(Bindings.size(CONTEXT.getModel().getTypesFiltered()).asString());
		countAllLabel.textProperty().bind(Bindings.size(CONTEXT.getModel().getTypes()).asString());
		//
		BorderPane pane = new BorderPane();
		pane.setLeft(new Label(CONST.getMatchingLabel()));
		pane.setRight(new HBox(countFoundLabel, countLabel, countAllLabel));
		return pane;
	}
	
	private Node createTop() {
		Label searchLabel = new Label(CONST.getSearchLabel());
		SEARCH_FIELD.setEditable(true);
		SEARCH_FIELD.setAlignment(Pos.CENTER);
		//
		SEARCH_FIELD.textProperty().addListener((observable, oldValue, newValue) -> {
			String pattern = newValue.toLowerCase();
			CONTEXT.getModel().getTypesFiltered().setPredicate(type -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				else {
					return type.getNameLowCase().startsWith(pattern);
				}
			});
		});
		//
		VBox top = new VBox(searchLabel, SEARCH_FIELD, createMatching());
		top.setSpacing(CONST.getSpacing());
		return top;
	}
	
	private Node createCenter() {
		ListView<Type> listView = new ListView<>();
		listView.setEditable(false);
		listView.setItems(CONTEXT.getModel().getTypesSorted());
		listView.setCellFactory((tableColumn) -> new UiSearchListCell(SEARCH_FIELD));
		listView.visibleProperty().bind(Bindings.isNotEmpty(CONTEXT.getModel().getTypesFiltered()));
		//
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Type>() {
			@Override
			public void changed(ObservableValue<? extends Type> observable, Type oldValue, Type newValue) {
				//METHOD_VIEW.setType(newValue);
			}
		});
		//
		BACKGROUND.setImage(CONST.getBackgroundImage());
		BACKGROUND.getPane().getChildren().add(listView);
		return BACKGROUND.getPane();
	}

	private Node createBottom() {
		Label locationLabel = new Label();
		locationLabel.textProperty().bind(CONTEXT.getModel().takeTypesSource());
		locationLabel.setGraphic(new ImageView(CONST.getPackageIcon()));
		locationLabel.setBorder(new TextField().getBorder());
		//
		VBox bottom = new VBox(locationLabel);
		bottom.setSpacing(CONST.getSpacing());
		return bottom;
	}
	
	private void buildForm() {
		PANE.setTop(createTop());
		PANE.setCenter(createCenter());
		PANE.setBottom(createBottom());
		//
		BorderPane.setMargin(PANE.getCenter(), new Insets(CONST.getSpacing(), 0, CONST.getSpacing(), 0));
		PANE.setPadding(new Insets(CONST.getPadding(), CONST.getPadding(), CONST.getPadding(), CONST.getPadding()));
	}
	
	@Override
	public final Pane getPane() {
		return PANE;
	}

}
