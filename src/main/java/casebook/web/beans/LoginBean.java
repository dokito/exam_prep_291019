package casebook.web.beans;

import casebook.domain.models.binding.LoginBindingModel;
import casebook.domain.models.service.UserServiceModel;
import casebook.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

@Named
@RequestScoped
public class LoginBean extends BaseBean {

    private LoginBindingModel model;

    private UserService userService;
    private ModelMapper modelMapper;

    public LoginBean() {
    }

    @Inject
    public LoginBean(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void init() {
        this.model = new LoginBindingModel();
    }

    public void login() {
        UserServiceModel user = this.userService.getByUsernameAndPassword(
                this.model.getUsername(), DigestUtils.sha256Hex(this.model.getPassword()));

        if (user == null){
            this.redirect("/login");
        }

        Map<String, Object> sessionMap = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap();
        sessionMap.put("username", this.model.getUsername());
        sessionMap.put("userId", user.getId());

        this.redirect("/home");
    }

    public LoginBindingModel getModel(){
        return model;
    }

    public void setModel(LoginBindingModel model) {
        this.model = model;
    }
}
