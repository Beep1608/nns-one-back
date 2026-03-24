-- liquibase formatted sql

-- changeset tu_usuario:limpiar_esquemas runalways:true splitstatements:false
do $$ 
declare
    r record;
begin
    -- buscar todos los esquemas que no sean del sistema ni el public
    for r in (
        select schema_name 
        from information_schema.schemata 
        where schema_name not in ('public', 'information_schema', 'pg_catalog', 'pg_toast')
        and schema_name not like 'pg_%'
    ) loop
        -- usando "%s" logramos mantener todo en minúsculas y postgres lo acepta
        execute format('drop schema if exists "%s" cascade', r.schema_name);
    end loop;
end;
$$;