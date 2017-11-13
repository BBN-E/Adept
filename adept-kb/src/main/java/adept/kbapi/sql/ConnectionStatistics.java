/*
* ------
* Adept
* -----
* Copyright (C) 2012-2017 Raytheon BBN Technologies Corp.
* -----
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
* -------
*/

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


import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
  
/**
 * 
 * @author dkolas
 */
public class ConnectionStatistics {

	private int queries = 0;
	
	private int connections = 0;
	
	private synchronized void incrementQueries(){
		queries++;
	}
	
	private synchronized void incrementConnections(){
		connections++;
	}
	
	public Connection wrapConnection(Connection connection){
		incrementConnections();
		return new TrackedConnection(connection);
	}
	
	public int getQueries(){
		return queries;
	}
	
	public int getConnections(){
		return connections;
	}
	
	public class TrackedConnection implements Connection{

		private Connection wrappedConnection;
		
		private TrackedConnection(Connection wrappedConnection){
			this.wrappedConnection = wrappedConnection;
		}

		/**
		 * @param iface
		 * @return
		 * @throws SQLException
		 * @see java.sql.Wrapper#unwrap(java.lang.Class)
		 */
		public <T> T unwrap(Class<T> iface) throws SQLException {
			return wrappedConnection.unwrap(iface);
		}

		/**
		 * @param iface
		 * @return
		 * @throws SQLException
		 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
		 */
		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return wrappedConnection.isWrapperFor(iface);
		}

		/**
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#createStatement()
		 */
		public Statement createStatement() throws SQLException {
			incrementQueries();
			return wrappedConnection.createStatement();
		}

		/**
		 * @param sql
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#prepareStatement(java.lang.String)
		 */
		public PreparedStatement prepareStatement(String sql) throws SQLException {
			incrementQueries();
			return wrappedConnection.prepareStatement(sql);
		}

		/**
		 * @param sql
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#prepareCall(java.lang.String)
		 */
		public CallableStatement prepareCall(String sql) throws SQLException {
			incrementQueries();
			return wrappedConnection.prepareCall(sql);
		}

		/**
		 * @param sql
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#nativeSQL(java.lang.String)
		 */
		public String nativeSQL(String sql) throws SQLException {
			return wrappedConnection.nativeSQL(sql);
		}

		/**
		 * @param autoCommit
		 * @throws SQLException
		 * @see java.sql.Connection#setAutoCommit(boolean)
		 */
		public void setAutoCommit(boolean autoCommit) throws SQLException {
			wrappedConnection.setAutoCommit(autoCommit);
		}

		/**
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#getAutoCommit()
		 */
		public boolean getAutoCommit() throws SQLException {
			return wrappedConnection.getAutoCommit();
		}

		/**
		 * @throws SQLException
		 * @see java.sql.Connection#commit()
		 */
		public void commit() throws SQLException {
			wrappedConnection.commit();
		}

		/**
		 * @throws SQLException
		 * @see java.sql.Connection#rollback()
		 */
		public void rollback() throws SQLException {
			wrappedConnection.rollback();
		}

		/**
		 * @throws SQLException
		 * @see java.sql.Connection#close()
		 */
		public void close() throws SQLException {
			wrappedConnection.close();
		}

		/**
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#isClosed()
		 */
		public boolean isClosed() throws SQLException {
			return wrappedConnection.isClosed();
		}

		/**
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#getMetaData()
		 */
		public DatabaseMetaData getMetaData() throws SQLException {
			return wrappedConnection.getMetaData();
		}

		/**
		 * @param readOnly
		 * @throws SQLException
		 * @see java.sql.Connection#setReadOnly(boolean)
		 */
		public void setReadOnly(boolean readOnly) throws SQLException {
			wrappedConnection.setReadOnly(readOnly);
		}

		/**
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#isReadOnly()
		 */
		public boolean isReadOnly() throws SQLException {
			return wrappedConnection.isReadOnly();
		}

		/**
		 * @param catalog
		 * @throws SQLException
		 * @see java.sql.Connection#setCatalog(java.lang.String)
		 */
		public void setCatalog(String catalog) throws SQLException {
			wrappedConnection.setCatalog(catalog);
		}

		/**
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#getCatalog()
		 */
		public String getCatalog() throws SQLException {
			return wrappedConnection.getCatalog();
		}

		/**
		 * @param level
		 * @throws SQLException
		 * @see java.sql.Connection#setTransactionIsolation(int)
		 */
		public void setTransactionIsolation(int level) throws SQLException {
			wrappedConnection.setTransactionIsolation(level);
		}

		/**
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#getTransactionIsolation()
		 */
		public int getTransactionIsolation() throws SQLException {
			return wrappedConnection.getTransactionIsolation();
		}

		/**
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#getWarnings()
		 */
		public SQLWarning getWarnings() throws SQLException {
			return wrappedConnection.getWarnings();
		}

		/**
		 * @throws SQLException
		 * @see java.sql.Connection#clearWarnings()
		 */
		public void clearWarnings() throws SQLException {
			wrappedConnection.clearWarnings();
		}

		/**
		 * @param resultSetType
		 * @param resultSetConcurrency
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#createStatement(int, int)
		 */
		public Statement createStatement(int resultSetType, int resultSetConcurrency)
				throws SQLException {
			incrementQueries();
			return wrappedConnection.createStatement(resultSetType, resultSetConcurrency);
		}

		/**
		 * @param sql
		 * @param resultSetType
		 * @param resultSetConcurrency
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
		 */
		public PreparedStatement prepareStatement(String sql, int resultSetType,
				int resultSetConcurrency) throws SQLException {
			incrementQueries();
			return wrappedConnection.prepareStatement(sql, resultSetType, resultSetConcurrency);
		}

		/**
		 * @param sql
		 * @param resultSetType
		 * @param resultSetConcurrency
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
		 */
		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
				throws SQLException {
			incrementQueries();
			return wrappedConnection.prepareCall(sql, resultSetType, resultSetConcurrency);
		}

		/**
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#getTypeMap()
		 */
		public Map<String, Class<?>> getTypeMap() throws SQLException {
			return wrappedConnection.getTypeMap();
		}

		/**
		 * @param map
		 * @throws SQLException
		 * @see java.sql.Connection#setTypeMap(java.util.Map)
		 */
		public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
			wrappedConnection.setTypeMap(map);
		}

		/**
		 * @param holdability
		 * @throws SQLException
		 * @see java.sql.Connection#setHoldability(int)
		 */
		public void setHoldability(int holdability) throws SQLException {
			wrappedConnection.setHoldability(holdability);
		}

		/**
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#getHoldability()
		 */
		public int getHoldability() throws SQLException {
			return wrappedConnection.getHoldability();
		}

		/**
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#setSavepoint()
		 */
		public Savepoint setSavepoint() throws SQLException {
			return wrappedConnection.setSavepoint();
		}

		/**
		 * @param name
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#setSavepoint(java.lang.String)
		 */
		public Savepoint setSavepoint(String name) throws SQLException {
			return wrappedConnection.setSavepoint(name);
		}

		/**
		 * @param savepoint
		 * @throws SQLException
		 * @see java.sql.Connection#rollback(java.sql.Savepoint)
		 */
		public void rollback(Savepoint savepoint) throws SQLException {
			wrappedConnection.rollback(savepoint);
		}

		/**
		 * @param savepoint
		 * @throws SQLException
		 * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
		 */
		public void releaseSavepoint(Savepoint savepoint) throws SQLException {
			wrappedConnection.releaseSavepoint(savepoint);
		}

		/**
		 * @param resultSetType
		 * @param resultSetConcurrency
		 * @param resultSetHoldability
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#createStatement(int, int, int)
		 */
		public Statement createStatement(int resultSetType, int resultSetConcurrency,
				int resultSetHoldability) throws SQLException {
			incrementQueries();
			return wrappedConnection.createStatement(resultSetType, resultSetConcurrency,
					resultSetHoldability);
		}

		/**
		 * @param sql
		 * @param resultSetType
		 * @param resultSetConcurrency
		 * @param resultSetHoldability
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
		 */
		public PreparedStatement prepareStatement(String sql, int resultSetType,
				int resultSetConcurrency, int resultSetHoldability) throws SQLException {
			incrementQueries();
			return wrappedConnection.prepareStatement(sql, resultSetType, resultSetConcurrency,
					resultSetHoldability);
		}

		/**
		 * @param sql
		 * @param resultSetType
		 * @param resultSetConcurrency
		 * @param resultSetHoldability
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
		 */
		public CallableStatement prepareCall(String sql, int resultSetType,
				int resultSetConcurrency, int resultSetHoldability) throws SQLException {
			incrementQueries();
			return wrappedConnection.prepareCall(sql, resultSetType, resultSetConcurrency,
					resultSetHoldability);
		}

		/**
		 * @param sql
		 * @param autoGeneratedKeys
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#prepareStatement(java.lang.String, int)
		 */
		public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
				throws SQLException {
			incrementQueries();
			return wrappedConnection.prepareStatement(sql, autoGeneratedKeys);
		}

		/**
		 * @param sql
		 * @param columnIndexes
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
		 */
		public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
				throws SQLException {
			incrementQueries();
			return wrappedConnection.prepareStatement(sql, columnIndexes);
		}

		/**
		 * @param sql
		 * @param columnNames
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
		 */
		public PreparedStatement prepareStatement(String sql, String[] columnNames)
				throws SQLException {
			incrementQueries();
			return wrappedConnection.prepareStatement(sql, columnNames);
		}

		/**
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#createClob()
		 */
		public Clob createClob() throws SQLException {
			return wrappedConnection.createClob();
		}

		/**
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#createBlob()
		 */
		public Blob createBlob() throws SQLException {
			return wrappedConnection.createBlob();
		}

		/**
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#createNClob()
		 */
		public NClob createNClob() throws SQLException {
			return wrappedConnection.createNClob();
		}

		/**
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#createSQLXML()
		 */
		public SQLXML createSQLXML() throws SQLException {
			return wrappedConnection.createSQLXML();
		}

		/**
		 * @param timeout
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#isValid(int)
		 */
		public boolean isValid(int timeout) throws SQLException {
			return wrappedConnection.isValid(timeout);
		}

		/**
		 * @param name
		 * @param value
		 * @throws SQLClientInfoException
		 * @see java.sql.Connection#setClientInfo(java.lang.String, java.lang.String)
		 */
		public void setClientInfo(String name, String value) throws SQLClientInfoException {
			wrappedConnection.setClientInfo(name, value);
		}

		/**
		 * @param properties
		 * @throws SQLClientInfoException
		 * @see java.sql.Connection#setClientInfo(java.util.Properties)
		 */
		public void setClientInfo(Properties properties) throws SQLClientInfoException {
			wrappedConnection.setClientInfo(properties);
		}

		/**
		 * @param name
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#getClientInfo(java.lang.String)
		 */
		public String getClientInfo(String name) throws SQLException {
			return wrappedConnection.getClientInfo(name);
		}

		/**
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#getClientInfo()
		 */
		public Properties getClientInfo() throws SQLException {
			return wrappedConnection.getClientInfo();
		}

		/**
		 * @param typeName
		 * @param elements
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#createArrayOf(java.lang.String, java.lang.Object[])
		 */
		public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
			return wrappedConnection.createArrayOf(typeName, elements);
		}

		/**
		 * @param typeName
		 * @param attributes
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#createStruct(java.lang.String, java.lang.Object[])
		 */
		public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
			return wrappedConnection.createStruct(typeName, attributes);
		}

		/**
		 * @param schema
		 * @throws SQLException
		 * @see java.sql.Connection#setSchema(java.lang.String)
		 */
		public void setSchema(String schema) throws SQLException {
			wrappedConnection.setSchema(schema);
		}

		/**
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#getSchema()
		 */
		public String getSchema() throws SQLException {
			return wrappedConnection.getSchema();
		}

		/**
		 * @param executor
		 * @throws SQLException
		 * @see java.sql.Connection#abort(java.util.concurrent.Executor)
		 */
		public void abort(Executor executor) throws SQLException {
			wrappedConnection.abort(executor);
		}

		/**
		 * @param executor
		 * @param milliseconds
		 * @throws SQLException
		 * @see java.sql.Connection#setNetworkTimeout(java.util.concurrent.Executor, int)
		 */
		public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
			wrappedConnection.setNetworkTimeout(executor, milliseconds);
		}

		/**
		 * @return
		 * @throws SQLException
		 * @see java.sql.Connection#getNetworkTimeout()
		 */
		public int getNetworkTimeout() throws SQLException {
			return wrappedConnection.getNetworkTimeout();
		}
		
		
		
	}
}