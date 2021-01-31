/*
 *
 *
 *          Copyright (c) 2021. - TinyZ.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.okra.rules.exception;

public class NoSuchParameterException extends RuntimeException {

    private static final long serialVersionUID = -6284005190319121589L;
    private String missingParameter;

    public NoSuchParameterException(String message, String missingParameter) {
        super(message);
        this.missingParameter = missingParameter;
    }

    public String getMissingParameter() {
        return missingParameter;
    }
}
