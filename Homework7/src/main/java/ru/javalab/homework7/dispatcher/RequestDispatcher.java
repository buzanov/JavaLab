package ru.javalab.homework7.dispatcher;

import ru.javalab.homework7.models.User;
import ru.javalab.homework7.protocol.Request;
import ru.javalab.homework7.protocol.Response;
import ru.javalab.homework7.protocol.json.JsonMessage;
import ru.javalab.homework7.services.JsonPackageService;
import ru.javalab.homework7.services.JsonResolveService;
import ru.javalab.homework7.services.LogicService;

import java.sql.ResultSet;
import java.util.LinkedHashMap;

public class RequestDispatcher {
    JsonResolveService jsonResolveService = new JsonResolveService();
    JsonPackageService jsonPackageService = new JsonPackageService();
    LogicService logicService = new LogicService();

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
                    response = logicService.registerLogic(request);
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
                            response = logicService.getMessages(request);
                            break;
                        }
                        case ("BuyProduct"): {
                            response = logicService.buyProduct(user, request);
                            break;
                        }
                        case ("DeleteProduct"): {
                            response = logicService.deleteProduct(user, request);
                            break;
                        }
                        case ("GetMyProducts"): {
                            response = logicService.getMyProducts(user);
                            break;
                        }
                        case ("GetAllProducts"): {
                            response = logicService.getAllProducts();
                            break;
                        }
                        case ("AddProduct"): {
                            response = logicService.addProduct(user, request);
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