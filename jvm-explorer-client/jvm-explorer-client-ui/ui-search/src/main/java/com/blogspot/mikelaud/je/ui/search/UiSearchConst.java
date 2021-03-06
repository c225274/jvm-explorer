package com.blogspot.mikelaud.je.ui.search;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Path;

import com.google.inject.BindingAnnotation;

public interface UiSearchConst {

	String getName();
	Path getBackgroundImage();
	Path getPackageIcon();
	//
	String getSearchLabel();
	String getMatchingLabel();
	String getCountLabel();

	//------------------------------------------------------------------------

	@BindingAnnotation @Target({ElementType.FIELD, ElementType.PARAMETER}) @Retention(RetentionPolicy.RUNTIME)
	@interface Name {}
	//
	@BindingAnnotation @Target({ElementType.FIELD, ElementType.PARAMETER}) @Retention(RetentionPolicy.RUNTIME)
	@interface BackgroundImage {}
	//
	@BindingAnnotation @Target({ElementType.FIELD, ElementType.PARAMETER}) @Retention(RetentionPolicy.RUNTIME)
	@interface PackageIcon {}
	//
	//
	@BindingAnnotation @Target({ElementType.FIELD, ElementType.PARAMETER}) @Retention(RetentionPolicy.RUNTIME)
	@interface SearchLabel {}
	//
	@BindingAnnotation @Target({ElementType.FIELD, ElementType.PARAMETER}) @Retention(RetentionPolicy.RUNTIME)
	@interface MatchingLabel {}
	//
	@BindingAnnotation @Target({ElementType.FIELD, ElementType.PARAMETER}) @Retention(RetentionPolicy.RUNTIME)
	@interface CountLabel {}

}
