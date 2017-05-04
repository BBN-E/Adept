package adept.kbapi.sql;

/*-
 * #%L
 * adept-kb
 * %%
 * Copyright (C) 2012 - 2017 Raytheon BBN Technologies
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import adept.kbapi.KBConfigurationException;
import adept.kbapi.KBParameters;

import java.sql.*;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Utility class that interacts with the SQL metadata database. Provides
 * wrappers for connecting to database, executing queries and updates,
 * committing, rolling back and closing connections.
 * 
 */
public class QuickJDBC {
	private BasicDataSource connectionPool;
	private String defaultSchema;
	private static final int MAX_ACTIVE_CONNECTIONS = 50;
	private static final int MAX_IDLE_CONNECTIONS = 8;
	private static final int REMOVE_ABANDONED_TIMEOUT = 5 * 60; // in seconds
	private static final int MAX_CONNECTION_LIFETIME = 60 * 1000; // in milliseconds

	private ConnectionStatistics connectionStatistics;
	
	public QuickJDBC(KBParameters kbParameters) throws KBConfigurationException {
		this(kbParameters, null);
	}

	public QuickJDBC(KBParameters kbParameters, String defaultSchema)
			throws KBConfigurationException {
		this.defaultSchema = defaultSchema;
		connectionStatistics = new ConnectionStatistics(); 
		initialize(kbParameters);
	}

	/**
	 * initialize connection pool. This connection pool is to be used by the
	 * entire KB setup.
	 * 
	 * @throws KBConfigurationException
	 */
	private void initialize(KBParameters kbParameters) throws KBConfigurationException {
		if (kbParameters.metadataUrl.trim().equals("")) {
			throw new KBConfigurationException("No metadata URL in KBParameters.");
		}
		connectionPool = new BasicDataSource();
		connectionPool.setUrl(kbParameters.metadataUrl);
		connectionPool.setUsername(kbParameters.metadataUsername);
		connectionPool.setPassword(kbParameters.metadataPassword);
		connectionPool.setDriverClassName("org.postgresql.Driver");
		connectionPool.setInitialSize(1);
		connectionPool.setDefaultAutoCommit(false);
		connectionPool.setMaxTotal(MAX_ACTIVE_CONNECTIONS);
		connectionPool.setMaxIdle(MAX_IDLE_CONNECTIONS);
		connectionPool.setRemoveAbandonedTimeout(REMOVE_ABANDONED_TIMEOUT);
		connectionPool.setRemoveAbandonedOnBorrow(true);
		connectionPool.setRemoveAbandonedOnMaintenance(true);
		connectionPool.setMaxConnLifetimeMillis(MAX_CONNECTION_LIFETIME);
	}

	/**
	 * retrieve connection instance
	 */
	public Connection getConnection() throws SQLException {
		Connection connection = connectionPool.getConnection();
		if (defaultSchema != null) {
			PreparedStatement setSearchPathPreparedStmt = connection
					.prepareStatement("SET search_path TO " + defaultSchema);
			setSearchPathPreparedStmt.executeUpdate();
			setSearchPathPreparedStmt.close();
		}
		return connectionStatistics.wrapConnection(connection);
	}

	/**
	 * execute database update
	 */
	public void executeSqlUpdate(PreparedStatement preparedStmt) throws SQLException {
		try {
			preparedStmt.executeUpdate();
		} finally {
			preparedStmt.close();
		}
	}

	/**
	 * execute query to check if object exists in the database. This closes the
	 * prepared statement passed in
	 */
	public boolean recordExists(PreparedStatement preparedStmt) throws SQLException {
		boolean doesRecordExist = false;
		java.sql.ResultSet rs = null;
		try {
			rs = preparedStmt.executeQuery();
			doesRecordExist = rs.next();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			;
			try {
				if (preparedStmt != null)
					preparedStmt.close();
			} catch (Exception e) {
			}
			;
		}
		return doesRecordExist;
	}

	public void printPoolInfo() {
		if (connectionPool != null) {
			System.out.println("Connection pool active connections: "
					+ connectionPool.getNumActive());
			System.out.println("Connection pool idle connections: " + connectionPool.getNumIdle());
		}
	}

	public void close() throws SQLException {
		connectionPool.close();
	}
	
	public ConnectionStatistics getConnectionStatistics(){
		return connectionStatistics;
	}
}
