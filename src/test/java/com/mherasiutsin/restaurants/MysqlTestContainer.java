package com.mherasiutsin.restaurants;

import org.testcontainers.containers.MySQLContainer;

public final class MysqlTestContainer extends MySQLContainer<MysqlTestContainer> {

    private static final String IMAGE_VERSION = "mysql:8";
    private static final int DB_PORT = 3306;
    private static MysqlTestContainer container;

    private MysqlTestContainer() {
        super(IMAGE_VERSION);
    }

    public static MysqlTestContainer getInstance() {
        if (container == null) {
            container = new MysqlTestContainer().withDatabaseName("restaurants").withExposedPorts(DB_PORT);
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", String.format("jdbc:mysql://%s:%s/%s", container.getHost(), container.getMappedPort(DB_PORT), container.getDatabaseName()));
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());

        System.setProperty(
                "spring.application.datasource.url",
                String.format("jdbc:mysql://%s:%s/%s", container.getHost(), container.getMappedPort(DB_PORT), container.getDatabaseName())
        );
        System.setProperty(
                "spring.application.datasource.jdbcUrl",
                String.format("jdbc:mysql://%s:%s/%s", container.getHost(), container.getMappedPort(DB_PORT), container.getDatabaseName())
        );
        System.setProperty("spring.application.datasource.username", container.getUsername());
        System.setProperty("spring.application.datasource.password", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }

}