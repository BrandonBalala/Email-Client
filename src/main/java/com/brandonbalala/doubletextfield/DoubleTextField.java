package com.brandonbalala.doubletextfield;

import javafx.scene.control.TextField;

/**
 * Custom JavaFX control that specializes TextField. See the article "Creating a
 * Custom JavaFX Control Using Inheritance and Making It Available in
 * SceneBuilder" for more information.
 * 
 * @author Ken Fogel
 */
public class DoubleTextField extends TextField {

	public DoubleTextField() {
		super();
	}

	// http://utilitymill.com/utility/Regex_For_Range
	String numberRegEx = "\\b([0-9]{1,2}|[1-6][0-9]{2}|7[0-3][0-9]|74[0-4])\\b";

	@Override
	public void replaceText(int start, int end, String text) {
		String oldValue = getText();
		if ((validate(text))) {
			super.replaceText(start, end, text);
			String newText = super.getText();
			if (!validate(newText)) {
				super.setText(oldValue);
			}
		}
	}

	@Override
	public void replaceSelection(String text) {
		String oldValue = getText();
		if (validate(text)) {
			super.replaceSelection(text);
			String newText = super.getText();
			if (!validate(newText)) {
				super.setText(oldValue);
			}
		}
	}

	private boolean validate(String text) {
		return ("".equals(text) || text.matches(numberRegEx));
	}
}

