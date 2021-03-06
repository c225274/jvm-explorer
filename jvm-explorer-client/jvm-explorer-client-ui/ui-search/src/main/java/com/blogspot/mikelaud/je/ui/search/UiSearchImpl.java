package com.blogspot.mikelaud.je.ui.search;

import com.blogspot.mikelaud.je.domain.pojo.DomainType;
import com.blogspot.mikelaud.je.mvc.MvcController;
import com.blogspot.mikelaud.je.mvc.MvcModel;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class UiSearchImpl implements UiSearch {

	private final MvcController CONTROLLER;
	private final MvcModel MODEL;
	private final UiSearchConst CONST;
	private final UiBackground BACKGROUND;
	//
	private final BorderPane PANE;
	private final TextField SEARCH_FIELD;

	@Inject
	private UiSearchImpl
	(	MvcController aController
	,	MvcModel aModel
	,	UiSearchConst aConst
	,	UiBackground aBackground
	) {
		CONTROLLER = aController;
		MODEL = aModel;
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
		countFoundLabel.textProperty().bind(Bindings.size(CONTROLLER.getDomain().getTypesFiltered()).asString());
		countAllLabel.textProperty().bind(Bindings.size(CONTROLLER.getDomain().getTypes()).asString());
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
			CONTROLLER.getDomain().getTypesFiltered().setPredicate(type -> {
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
		//top.setSpacing(CONST.getSpacing());
		return top;
	}

	private Node createCenter() {
		ListView<DomainType> listView = new ListView<>();
		listView.setEditable(false);
		listView.setItems(CONTROLLER.getDomain().getTypesSorted());
		listView.setCellFactory((tableColumn) -> new UiSearchListCell(CONTROLLER, SEARCH_FIELD));
		listView.visibleProperty().bind(Bindings.isNotEmpty(CONTROLLER.getDomain().getTypesFiltered()));
		//
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DomainType>() {
			@Override
			public void changed(ObservableValue<? extends DomainType> observable, DomainType oldValue, DomainType newValue) {
				CONTROLLER.showCode(newValue);
			}
		});
		//
		BACKGROUND.setImage(MODEL.getImage(CONST.getBackgroundImage()));
		BACKGROUND.getPane().getChildren().add(listView);
		return BACKGROUND.getPane();
	}

	private void buildForm() {
		PANE.setTop(createTop());
		PANE.setCenter(createCenter());
		//
		PANE.setPadding(new Insets(MODEL.getConst().getPadding()));
	}

	@Override public String getName() { return CONST.getName(); }
	@Override public final Pane getPane() { return PANE; }

}
