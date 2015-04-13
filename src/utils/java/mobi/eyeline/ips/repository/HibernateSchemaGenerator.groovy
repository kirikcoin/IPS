package mobi.eyeline.ips.repository

import org.hibernate.cfg.Configuration
import org.hibernate.tool.hbm2ddl.SchemaExport

import static org.hibernate.tool.hbm2ddl.Target.SCRIPT

def configuration = new Configuration()
    .configure("/hibernate-model.cfg.xml")
    .configure()

new SchemaExport(configuration).with {
  delimiter = ";"
  outputFile = "ips_schema.sql"
  create(SCRIPT)
}
