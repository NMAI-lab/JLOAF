package org.jLOAF.retrieve;

import java.util.List;

import org.jLOAF.casebase.Case;
import org.jLOAF.inputs.Input;

public interface Retrieval {

	public List<Case> retrieve(Input i);
}
