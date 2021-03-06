/*
	Copyright (c) 2009-2011
		Speech Group at Informatik 5, Univ. Erlangen-Nuremberg, GERMANY
		Korbinian Riedhammer
		Tobias Bocklet

	This file is part of the Java Speech Toolkit (JSTK).

	The JSTK is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	The JSTK is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with the JSTK. If not, see <http://www.gnu.org/licenses/>.
*/
package com.github.sikoried.jstk.exceptions;

import java.io.IOException;

public class InvalidFormatException extends IOException {
	private static final long serialVersionUID = 1L;
	
	public String problem;
	public String fileName;
	public long lineNumber;
	
	public InvalidFormatException(String fileName, long lineNumber, String problem) {
		this.problem = problem;
		this.lineNumber = lineNumber;
		this.fileName = fileName;
	}
	
	public String toString() {
		return "InvalidFormatException in " + fileName + ":" + lineNumber + ": " + problem;
	}
}
