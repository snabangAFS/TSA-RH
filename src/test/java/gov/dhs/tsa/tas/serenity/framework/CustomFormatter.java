package gov.dhs.tsa.tas.serenity.framework;

import gherkin.formatter.Reporter;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;

public class CustomFormatter implements Reporter {

	@Override
	public void before(Match match, Result result) {
	}

	@Override
	public void result(Result result) {
		if (result.getErrorMessage() != null) {
			System.out.println(result.getErrorMessage());
		}
	}

	@Override
	public void after(Match match, Result result) {
	}

	@Override
	public void match(Match match) {
	}

	@Override
	public void embedding(String s, byte[] bytes) {
	}

	@Override
	public void write(String s) {
	}

}