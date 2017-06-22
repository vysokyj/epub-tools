/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.slf4j.impl;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.helpers.MessageFormatter;

/**
 * Delegates the actual logging to the {@link org.apache.tools.ant.BuildListener}s configured on the ant {@link org.apache.tools.ant.Project} the code using
 * slf4j runs in.
 *
 * @see #setProject(org.apache.tools.ant.Project)
 * @author Manuel Amoabeng
 */
public class AntLogger implements Logger {

    class Slf4jBuildEvent extends BuildEvent {

        private final int priority;
        private final String format;
        private final Object[] args;

        private String message;

        Slf4jBuildEvent(String format, int priority, Throwable exception, Object[] args) {
            super(project);
            if (exception != null) {
                setException(exception);
            }
            this.priority = priority;
            this.format = format;
            this.args = args;
        }

        @Override
        public String getMessage() {
            if (message == null) {
                message = MessageFormatter.arrayFormat(format, args).getMessage();
            }
            return message;
        }

        @Override
        public int getPriority() {
            return priority;
        }
    }

    private static Project project;


    /**
     * Set up the project here before executing code using slf4j in your ant {@link org.apache.tools.ant.Task}s.
     *
     * @param project The ant {@link org.apache.tools.ant.Project} containing the {@link org.apache.tools.ant.BuildListener}s the {@link org.slf4j.Logger}s should delegate to
     */
    public static void setProject(Project project) {
        AntLogger.project = project;
    }

    private final String name;

    public AntLogger(String name) {
        this.name = name;
    }

    private void log(String message, int priority, Throwable exception, Object... args) {
        if (project == null) {
            return;
        }
        BuildEvent buildEvent = new Slf4jBuildEvent(message, priority, exception, args);
        for (Object buildListener : project.getBuildListeners()) {
            ((BuildListener) buildListener).messageLogged(buildEvent);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public void trace(String msg) {
        log(msg, Project.MSG_VERBOSE, null);
    }

    @Override
    public void trace(String format, Object arg) {
        log(format, Project.MSG_VERBOSE, null, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        log(format, Project.MSG_VERBOSE, null, arg1, arg2);
    }

    @Override
    public void trace(String format, Object[] argArray) {
        log(format, Project.MSG_VERBOSE, null, argArray);
    }

    @Override
    public void trace(String msg, Throwable t) {
        log(msg, Project.MSG_VERBOSE, t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return true;
    }

    @Override
    public void trace(Marker marker, String msg) {
        log(msg, Project.MSG_VERBOSE, null);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        log(format, Project.MSG_VERBOSE, null, arg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        log(format, Project.MSG_VERBOSE, null, arg1, arg2);
    }

    @Override
    public void trace(Marker marker, String format, Object[] argArray) {
        log(format, Project.MSG_VERBOSE, null, argArray);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        log(msg, Project.MSG_VERBOSE, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public void debug(String msg) {
        log(msg, Project.MSG_DEBUG, null);
    }

    @Override
    public void debug(String format, Object arg) {
        log(format, Project.MSG_DEBUG, null, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        log(format, Project.MSG_DEBUG, null, arg1, arg2);
    }

    @Override
    public void debug(String format, Object[] argArray) {
        log(format, Project.MSG_DEBUG, null, argArray);
    }

    @Override
    public void debug(String msg, Throwable t) {
        log(msg, Project.MSG_DEBUG, t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return true;
    }

    @Override
    public void debug(Marker marker, String msg) {
        log(msg, Project.MSG_DEBUG, null);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        log(format, Project.MSG_DEBUG, null, arg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        log(format, Project.MSG_DEBUG, null, arg1, arg2);
    }

    @Override
    public void debug(Marker marker, String format, Object[] argArray) {
        log(format, Project.MSG_DEBUG, null, argArray);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        log(msg, Project.MSG_DEBUG, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public void info(String msg) {
        log(msg, Project.MSG_INFO, null);
    }

    @Override
    public void info(String format, Object arg) {
        log(format, Project.MSG_INFO, null, arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        log(format, Project.MSG_INFO, null, arg1, arg2);
    }

    @Override
    public void info(String format, Object[] argArray) {
        log(format, Project.MSG_INFO, null, argArray);
    }

    @Override
    public void info(String msg, Throwable t) {
        log(msg, Project.MSG_INFO, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return true;
    }

    @Override
    public void info(Marker marker, String msg) {
        log(msg, Project.MSG_INFO, null);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        log(format, Project.MSG_INFO, null, arg);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        log(format, Project.MSG_INFO, null, arg1, arg2);
    }

    @Override
    public void info(Marker marker, String format, Object[] argArray) {
        log(format, Project.MSG_INFO, null, argArray);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        log(msg, Project.MSG_INFO, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public void warn(String msg) {
        log(msg, Project.MSG_WARN, null);
    }

    @Override
    public void warn(String format, Object arg) {
        log(format, Project.MSG_WARN, null, arg);
    }

    @Override
    public void warn(String format, Object[] argArray) {
        log(format, Project.MSG_WARN, null, argArray);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        log(format, Project.MSG_WARN, null, arg1, arg2);
    }

    @Override
    public void warn(String msg, Throwable t) {
        log(msg, Project.MSG_WARN, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return true;
    }

    @Override
    public void warn(Marker marker, String msg) {
        log(msg, Project.MSG_WARN, null);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        log(format, Project.MSG_WARN, null, arg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        log(format, Project.MSG_WARN, null, arg1, arg2);
    }

    @Override
    public void warn(Marker marker, String format, Object[] argArray) {
        log(format, Project.MSG_WARN, null, argArray);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        log(msg, Project.MSG_WARN, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public void error(String msg) {
        log(msg, Project.MSG_ERR, null);
    }

    @Override
    public void error(String format, Object arg) {
        log(format, Project.MSG_ERR, null, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        log(format, Project.MSG_ERR, null, arg1, arg2);
    }

    @Override
    public void error(String format, Object[] argArray) {
        log(format, Project.MSG_ERR, null, argArray);
    }

    @Override
    public void error(String msg, Throwable t) {
        log(msg, Project.MSG_ERR, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return true;
    }

    @Override
    public void error(Marker marker, String msg) {
        log(msg, Project.MSG_ERR, null);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        log(format, Project.MSG_ERR, null, arg);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        log(format, Project.MSG_ERR, null, arg1, arg2);
    }

    @Override
    public void error(Marker marker, String format, Object[] argArray) {
        log(format, Project.MSG_ERR, null, argArray);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        log(msg, Project.MSG_ERR, t);
    }
}
