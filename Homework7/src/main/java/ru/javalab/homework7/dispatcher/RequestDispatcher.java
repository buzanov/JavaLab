package ru.javalab.homework7.dispatcher;

import ru.javalab.context.Component;
import ru.javalab.homework7.models.User;
import ru.javalab.homework7.protocol.Request;
import ru.javalab.homework7.protocol.Response;
import ru.javalab.homework7.services.JsonPackageService;
import ru.javalab.homework7.services.JsonResolveService;
import ru.javalab.homework7.services.LogicService;

import java.util.LinkedHashMap;

public class RequestDispatcher implements Component {
    JsonResolveService jsonResolveService;
    JsonPackageService jsonPackageService;
    LogicService logicService;

    public Response doDispatch(Request request) {
        User user = jsonResolveService.getUserFromToken(request.getToken());
        Response response = null;
        switch (request.getHeader()) {
            case ("Message"): {
                response = logicService.messageLogic(user, request);
                break;
            }
            case ("Login"): {
                if (user == null) {
                    response = logicService.loginLogic(request);
                } else {
                    response = jsonPackageService.ERROR_MESSAGE_ALREADY_LOGGED_IN;
                }
                break;
            }
            case ("Register"): {
                if (user == null) {
                    response = registerLogic(request);
                } else {
                    response = jsonPackageService.ERROR_MESSAGE_ALREADY_LOGGED_IN;
                }
                break;
            }
            case ("Command"): {
                if (user == null) {
                    response = jsonPackageService.ERROR_MESSAGE_NOT_AUTHORIZED;
                } else {
                    LinkedHashMap payload = (LinkedHashMap) request.getPayload();
                    String command = (String) payload.get("command");
                    switch (command) {
                        case ("GetMessages"): {
                            response = getMessages(request);
                            break;
                        }
                        case ("BuyProduct"): {
                            response = buyProduct(user, request);
                            break;
                        }
                        case ("DeleteProduct"): {
                            response = deleteProduct(user, request);
                            break;
                        }
                        case ("GetMyProducts"): {
                            response = getMyProducts(user);
                            break;
                        }
                        case ("GetAllProducts"): {
                            response = getAllProducts();
                            break;
                        }
                        case ("AddProduct"): {
                            response = addProduct(user, request);
                            break;
                        }

                    }

                }
            }

        }
        return response;
    }

    @Override
    public String getName() {
        return getClass().getName();
    }
}