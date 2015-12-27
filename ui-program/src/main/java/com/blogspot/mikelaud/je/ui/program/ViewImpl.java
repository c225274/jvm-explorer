package com.blogspot.mikelaud.je.ui.program;

import com.blogspot.mikelaud.je.mvc.search.VSearch;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ViewImpl implements View {

	private final ViewContext CONTEXT;
	private final ViewConst CONST;
	private final VSearch V_SEARCH;
	//
	private final SplitPane PANE;
	private final Scene SCENE;
	private final Stage STAGE;
	
	@Inject
	private ViewImpl
	(	ViewContext aContext
	,	ViewConst aConst
	,	VSearch aVSearch
	,	@Assisted Stage aStage
	) {
		CONTEXT = aContext;
		CONST = aConst;
		V_SEARCH = aVSearch;
		//
		PANE = new SplitPane();
		SCENE = new Scene(PANE);
		STAGE = aStage;
		//
		buildePane();
	}
	
	private void buildePane() {
		//FORM.getItems().addAll(OPEN_TYPE_VIEW.getForm(), OPEN_METHODS_VIEW.getForm());
		PANE.getItems().addAll(V_SEARCH.getPane());
		buildStage();
	}

	private void buildStage() {
		STAGE.setScene(SCENE);
		STAGE.setTitle(CONST.getProgramTitle());
		STAGE.getIcons().setAll(CONST.getProgramIcon());
		STAGE.fullScreenExitHintProperty().setValue(CONST.getEmptyHint());
		//
		Rectangle2D visualBounds = createVisualBounds();
		Rectangle2D defaultBounds = createDefaultBounds(visualBounds);
		//
		STAGE.setWidth(defaultBounds.getWidth());
		STAGE.setHeight(defaultBounds.getHeight());
		//
		STAGE.setMinWidth(STAGE.getWidth());
		STAGE.setMinHeight(STAGE.getHeight());
		//
		STAGE.setMaxWidth(visualBounds.getWidth());
		STAGE.setMaxHeight(visualBounds.getHeight());
	}

	private Rectangle2D createVisualBounds() {
		return Screen.getPrimary().getVisualBounds();
	}
	
	private Rectangle2D createDefaultBounds(Rectangle2D aVisualBounds) {
		double defaultWidth = aVisualBounds.getWidth() / CONST.getScaleWidth();
		double defaultHeight = aVisualBounds.getHeight() / CONST.getScaleHeight();
		return new Rectangle2D(0, 0, defaultWidth, defaultHeight);
	}	
	
	@Override
	public final void show() {
		if (! STAGE.isShowing()) {
			STAGE.show();
			Platform.runLater(() -> CONTEXT.getController().setDefaultTypes());
		}
	}
	
}