package org.jLOAF.retrieve;

import java.util.List;

import org.jLOAF.casebase.Case;
import org.jLOAF.inputs.Input;
import org.jLOAF.retrieve.Distance;
/**
 * Interface of retrieval
 * @author sachagunaratne
 *
 */
public interface Retrieval {
	/**
	 * Returns a list of cases
	 * @param i
	 * @return A list of cases
	 */
	public List<Case> retrieve(Input i);

	/**
	 * Returns an array of closes distances
	 * @return an array of closest distances
	 */
	public Distance [] getDist();
}
