package DataAccess;

import javax.swing.*;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("* ");
        sb.append("FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    private String createSelectAllQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("* ");
        sb.append("FROM ");
        sb.append(type.getSimpleName());
        return sb.toString();
    }

    private String createInsertQuery(StringBuilder columns, StringBuilder values) {
        String query = "INSERT INTO " + type.getSimpleName() + " (" + columns + ") VALUES (" + values + ")";
        return query;
    }

    private String createUpdateQuery(StringBuilder setClause) {
        String query = "UPDATE " + type.getSimpleName() + " SET " + setClause + " WHERE id=?";
        return query;
    }

    private String createDeleteQuery() {
        String query ="DELETE FROM " + type.getSimpleName() + " WHERE id=?";
        return query;
    }

    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + " DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return null;
    }

    public ArrayList<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectAllQuery();
        ArrayList<T> list = new ArrayList<>();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + " DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return null;
    }

    public void insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            Field[] fields = type.getDeclaredFields();

            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();
            List<Object> fieldValues = new ArrayList<>();

            for (Field field : fields) {
                field.setAccessible(true);
                if (!field.getName().equalsIgnoreCase("id")) {
                    columns.append(field.getName()).append(",");
                    values.append("?,");
                    fieldValues.add(field.get(t));
                }
            }

            columns.setLength(columns.length() - 1);
            values.setLength(values.length() - 1);

            String query = createInsertQuery(columns, values);
            statement = connection.prepareStatement(query);

            for (int i = 0; i < fieldValues.size(); i++) {
                statement.setObject(i + 1, fieldValues.get(i));
            }
            statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + " DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    public void update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            Field[] fields = type.getDeclaredFields();

            StringBuilder setClause = new StringBuilder();
            List<Object> fieldValues = new ArrayList<>();
            Object idValue = null;

            for (Field field : fields) {
                field.setAccessible(true);
                if (!field.getName().equalsIgnoreCase("id")) {
                    setClause.append(field.getName()).append("=?, ");
                    fieldValues.add(field.get(t));
                } else {
                    idValue = field.get(t);
                }
            }

            setClause.setLength(setClause.length() - 2);

            String query = createUpdateQuery(setClause);
            statement = connection.prepareStatement(query);

            for (int i = 0; i < fieldValues.size(); i++) {
                statement.setObject(i + 1, fieldValues.get(i));
            }
            statement.setObject(fieldValues.size() + 1, idValue);

            statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + " DAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }


    public void delete(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();

            Field idField = type.getDeclaredField("id");
            idField.setAccessible(true);
            Object idValue = idField.get(t);

            String query = createDeleteQuery();
            statement = connection.prepareStatement(query);
            statement.setObject(1, idValue);

            statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + " DAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }



    private ArrayList<T> createObjects(ResultSet resultSet) {
        ArrayList<T> list = new ArrayList<>();

        try {
            while (resultSet.next()) {
                T instance = type.newInstance();

                for (Field field : type.getDeclaredFields()) {
                    Object value = resultSet.getObject(field.getName());

                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getWriteMethod();

                    method.invoke(instance, value);
                }

                list.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException | SQLException | IntrospectionException |
                 InvocationTargetException e) {
            LOGGER.log(Level.WARNING, type.getName() + " DAO:createObjects " + e.getMessage());
        }

        return list;
    }

    public static <T> JTable buildTableFromList(List<T> list) {
        if (list == null || list.isEmpty()) return new JTable();

        T objList = list.get(0);
        Field[] fields = objList.getClass().getDeclaredFields();
        String[] columnNames = Arrays.stream(fields).map(Field::getName).toArray(String[]::new);

        Object[][] data = new Object[list.size()][fields.length];
        for (int i = 0; i < list.size(); i++) {
            T obj = list.get(i);
            for (int j = 0; j < fields.length; j++) {
                fields[j].setAccessible(true);
                try {
                    data[i][j] = fields[j].get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return new JTable(data, columnNames);
    }


}
