package com.sample.instant.reactive.boot;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.util.ExceptionUtil;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

/**
 * Bean used to configure the liquibase execution. This hack is required because liquibase needs jdbc datasource whereas
 * reactive datasources are configured in the applicnecessitated by the use of reactive drivers to interact
 * with postgres. Liquibase expects jdbc driver to perform the migration.
 */
@ApplicationScoped
@Slf4j
public class LiquibaseInitAgent {

    @ConfigProperty(name = "custom.liquibase.migrate")
    boolean runMigration;
    @ConfigProperty(name = "quarkus.datasource.jdbc.url")
    String datasourceUrl;
    @ConfigProperty(name = "quarkus.datasource.username")
    String datasourceUsername;
    @ConfigProperty(name = "quarkus.datasource.password")
    String datasourcePassword;
    @ConfigProperty(name = "quarkus.liquibase.change-log")
    String changeLogLocation;

    /**
     * Method which listens to the start-up event for the application and configures the liquibase parameters for database
     * migration.
     *
     * @param event the application startup event
     * @throws LiquibaseException the liquibase exception thrown
     */
    public void runLiquibaseMigration(@Observes StartupEvent event) throws LiquibaseException {
        // Custom flag set to indicate if migration should proceed through the agent.
        if (runMigration) {
            Liquibase liquibase = null;
            try {
                ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(Thread.currentThread().getContextClassLoader());
                DatabaseConnection conn = DatabaseFactory.getInstance().openConnection(datasourceUrl, datasourceUsername, datasourcePassword, null, resourceAccessor);
                liquibase = new Liquibase(changeLogLocation, resourceAccessor, conn);
                liquibase.update(new Contexts(), new LabelExpression());
            } catch (Exception migrationException) {
                log.error("Liquibase Migration Exception: " + ExceptionUtil.generateStackTrace(migrationException));
            } finally {
                if (liquibase != null) {
                    liquibase.close();
                }
            }
        }
    }
}
