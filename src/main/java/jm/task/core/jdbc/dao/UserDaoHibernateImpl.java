package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private final SessionFactory sessionFactory = Util.getSessionFactory();
    private final Session session = sessionFactory.getCurrentSession();

    public UserDaoHibernateImpl() {
    }
    @Override
    public void createUsersTable() {
        try (sessionFactory; session) {
            session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS users " +
                    "(id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), last_name VARCHAR(255), age INT" +
                    ")").addEntity(User.class).executeUpdate();
            session.getTransaction().commit();
            sessionFactory.close();
            System.out.println("Table created");
        } catch (IllegalStateException e) {
                session.getTransaction().rollback();
        }
    }

    @Override
    public void dropUsersTable() {
        try (sessionFactory; session) {
            session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
            session.getTransaction().commit();
        } catch (IllegalStateException e) {
            session.getTransaction().rollback();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (sessionFactory; session) {
            session.beginTransaction();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
        } catch (IllegalStateException e) {
            if (session.getTransaction() != null  && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (sessionFactory; session) {
            session.beginTransaction();
            session.createSQLQuery("DELETE FROM users WHERE id = :id").setParameter("id", id).executeUpdate();
            session.getTransaction().commit();
        } catch (IllegalStateException e) {
            session.getTransaction().rollback();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (sessionFactory; session) {
            session.beginTransaction();
            return session.createQuery("FROM users", User.class).list();
        }
    }

    @Override
    public void cleanUsersTable() {
        try (sessionFactory; session) {
            session.beginTransaction();
            session.createQuery("DELETE FROM users").executeUpdate();
            session.getTransaction().commit();
        } catch (IllegalStateException e){
            session.getTransaction().rollback();
        }
    }
}
