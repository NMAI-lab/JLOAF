package org.jLOAF.retrieve;

import java.util.List;

import org.jLOAF.casebase.Case;
import org.jLOAF.inputs.Input;
import org.jLOAF.retrieve.Distance;

public interface Retrieval {

	public List<Case> retrieve(Input i);

	public Distance [] getDist();
}
