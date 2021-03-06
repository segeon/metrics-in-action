package lab.api;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import lab.model.Customer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;

@Component
@Path("/api/customers")
public class CustomerResource {

    private static final Log LOG = LogFactory.getLog(CustomerResource.class);

    private CustomerService service;

    @Autowired
    public CustomerResource(final CustomerService service) {
        this.service = service;
    }

    public CustomerResource() {
    }

    @GET
    @Path("/{customerId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Timed(name = "get-customer-timed")
    @ExceptionMetered(name = "get-customer-exception-meter")
    public Response getCustomer(@PathParam("customerId") final String customerId) {
        LOG.info("*** Customer Requested With Id: " + customerId);
        final Customer customer = service.findCustomer(customerId);
        if (customer == null) return Response.status(Status.NOT_FOUND).entity("Customer not found").build();
        return Response.ok(customer).build();
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Timed(name = "create-customer-timed")
    @ExceptionMetered(name = "create-customer-exception-meter")
    public Response createCustomer(final Customer customer) throws Exception {
        LOG.info("*** Customer Creation Requested: " + customer);
        final Customer createdCustomer = service.newCustomer(customer);
        return Response.created(new URI("/customer/" + customer.getId())).entity(createdCustomer).build();
    }

}
