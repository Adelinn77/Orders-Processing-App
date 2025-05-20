package DataAccess;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
import java.util.stream.Collectors;

/**
 * This class handles database operations for all entities.
 */

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

            List<Field> fieldList = Arrays.stream(fields)
                    .filter(f -> !f.getName().equalsIgnoreCase("id"))
                    .peek(f -> f.setAccessible(true))
                    .toList();

            String columnsStr = fieldList.stream()
                    .map(Field::getName)
                    .collect(Collectors.joining(","));
            String valuesStr = fieldList.stream()
                    .map(f -> "?")
                    .collect(Collectors.joining(","));

            List<Object> fieldValues = fieldList.stream()
                    .map(f -> {
                        try {
                            return f.get(t);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }).toList();

            String query = createInsertQuery(new StringBuilder(columnsStr), new StringBuilder(valuesStr));
            statement = connection.prepareStatement(query);
            System.out.println(query);

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

            Field idField = Arrays.stream(fields)
                    .filter(f -> f.getName().equalsIgnoreCase("id"))
                    .peek(f -> f.setAccessible(true))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No ID field found"));

            Object idValue = idField.get(t);

            List<Field> fieldList = Arrays.stream(fields)
                    .filter(f -> !f.getName().equalsIgnoreCase("id"))
                    .peek(f -> f.setAccessible(true))
                    .toList();

            String setClause = fieldList.stream()
                    .map(Field::getName)
                    .map(name -> name + "=?")
                    .collect(Collectors.joining(", "));

            List<Object> fieldValues = fieldList.stream()
                    .map(f -> {
                        try {
                            return f.get(t);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }).toList();

            String query = createUpdateQuery(new StringBuilder(setClause));
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
            e.printStackTrace();
        }

        return list;
    }

    public static <T> JTable buildTableFromList(List<T> list) {
        if (list == null || list.isEmpty()) return new JTable();

        T objList = list.get(0);
        Field[] fields = objList.getClass().getDeclaredFields();
        String[] columnNames = Arrays.stream(fields).map(Field::getName).toArray(String[]::new);

        Object[][] data = list.stream()
                .map(obj -> Arrays.stream(fields)
                        .map(field -> {
                            field.setAccessible(true);
                            try {
                                return field.get(obj);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }).toArray())
                .toArray(Object[][]::new);
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        return new JTable(model);
    }


}
