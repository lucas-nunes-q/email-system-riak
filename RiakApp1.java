/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

//Lucas Nunes Queiroz - 12011GIN005 - Projeto de app usando Riak


package com.facom39701.riakapp1;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.RiakException;
import com.basho.riak.client.core.RiakCluster;
import com.basho.riak.client.core.RiakNode;
import com.facom39701.riakapp1.model.Message;
import com.facom39701.riakapp1.model.Timeline;
import com.facom39701.riakapp1.model.TimelineType;
import com.facom39701.riakapp1.model.User;
import com.facom39701.riakapp1.repository.MessageRepository;
import com.facom39701.riakapp1.repository.TimelineRepository;
import com.facom39701.riakapp1.repository.UserRepository;
import java.rmi.UnknownHostException;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author Lucas Nunes
 */
public class RiakApp1 {

    private static RiakCluster setupCluster()
            throws UnknownHostException {
        RiakNode node = new RiakNode.Builder()
                .withRemoteAddress("0.0.0.0")
                .withRemotePort(8087)
                .build();
        RiakCluster cluster = new RiakCluster.Builder(node)
                .build();
        cluster.start();
        return cluster;
    }

    public static void main(String[] args)
            throws RiakException {
        try{
            RiakCluster cluster = setupCluster();
            RiakClient client = new RiakClient(cluster);

        System.out.println(" Client successfully created ");
        UserRepository userRepo = new UserRepository(client);
        MessageRepository msgRepo = new MessageRepository(client);
        TimelineRepository timelineRepo = new TimelineRepository(client);
        
        int select = -1;
        Scanner sc = new Scanner(System.in);
        
        loopfora:
        do{
            System.out.println("WhatChat");
            System.out.println("");
            System.out.println("(1) para login");
            System.out.println("(2) para cadastro");
            System.out.println("(0) para sair do aplicativo");
            System.out.println("");
            System.out.print("Digite uma das opcoes acima: ");  //Tela menu inicial
            
            select = Integer.parseInt(sc.nextLine());  //Converte String para int
            boolean acesso;  //Permite ou nao acesso ao sistem
            
                switch (select) {
                    case 1:
                        User user = new User();
                        System.out.print("Digite o seu nome de usuario: ");
                        user.setName(sc.nextLine());
                        System.out.print("Digite a sua senha: ");
                        user.setPassword(sc.nextLine());
                        user = userRepo.get(user.getName());
                        acesso = userRepo.get(user.getName()).equals(user); //Sistema de login (Nao esta funcionando)
                        
                        /*System.out.println(acesso);
                        System.out.println(user);
                        System.out.println(userRepo.get(user.getName()));*/
                        if (acesso == false) {  //Era para ser true, mas nao consegui implementar um metodo que compare os objetos corretamente
                            int select2 = -1;
                            Scanner sc2 = new Scanner(System.in);                                
                            do{
                                System.out.println("WhatChat");
                                System.out.println("");
                                System.out.println("(1) para ler a caixa de entrada de hoje");
                                System.out.println("(2) para ler a caixa de saida de hoje");
                                System.out.println("(3) para enviar uma nova mensagem");
                                System.out.println("(0) para sair do aplicativo");
                                System.out.println("");
                                System.out.print("Digite uma das opcoes acima: ");  //Menu pos-login
                                
                                select2 = Integer.parseInt(sc2.nextLine());
                                
                                    switch (select2){
                                        case 1:
                                            Timeline usersInboxToday = timelineRepo.getTimeline(
                                            user.getName(),
                                                TimelineType.INBOX,
                                                    new Date());

                                            System.out.println("Mensagens recebidas: ");
                                            for (String msgKey: usersInboxToday.getMessages()) {
                                                Message m = msgRepo.get(msgKey);
            
                                                System.out.println("\tDe: " + m.getSender());
                                                System.out.println("\tData: " + m.getCreatedAt());
                                                System.out.println("\tTexto : " + m.getText());
                                                System.out.println("");
                                            }  
                                            break;  //Mostra a caixa de mensagens recebidas do usuario
                                        case 2:
                                            Timeline usersSentToday = timelineRepo.getTimeline(
                                            user.getName(),
                                                TimelineType.SENT,
                                                new Date());
        
                                            System.out.println("Mensagens enviadas: ");
                                            for (String msgKey: usersSentToday.getMessages()) {
                                                Message m = msgRepo.get(msgKey);
            
                                                System.out.println("\tPara: " + m.getRecipient());
                                                System.out.println("\tData: " + m.getCreatedAt());
                                                System.out.println("\tTexto: " + m.getText());
                                                System.out.println("");
                                            }
                                            break;  //Mostra a caixa de mensagens enviadas pelo usuario
                                        case 3:
                                            int select3 = -2;
                                            Scanner sc3 = new Scanner(System.in);
                                            do{
                                                int index = 0;
                                                for (String l : userRepo.list()){
                                                    System.out.println("(" + (index++) + ")" + l);  //Mostra os usuarios na lista junto com o numero que os representa
                                                }
                                                System.out.print("Digite uma das opcoes acima: ");
                                                
                                                select3 = Integer.parseInt(sc3.nextLine());
                                                
                                                if (select3 > userRepo.list().size()) {
                                                    try {
                                                        userRepo.list().get(select3);
                                                    } catch (IndexOutOfBoundsException e) {
                                                        System.out.println("Opcao invalida!");  //Erro caso o usuario selecione algum numero que nao esta na lista
                                                    }
                                                } else {
                                                    String userMsg = userRepo.list().get(select3);
                                                    String text = "";
                                                    Scanner sc4 = new Scanner(System.in);
                                                    Message msg = new Message(user.getName(), userMsg, text);
                                                    System.out.print("Digite sua mensagem: ");
                                                    msg.setText(sc4.nextLine());
                                                    timelineRepo.postMessage(msg);
                                                    System.out.println("");
                                                    System.out.println("Mensagem enviada!");
                                                }
                                                break;  //Permite que o usuario mande uma mensagem para outro
                                            } while (select3 != -1);
                                            break;
                                        case 0:
                                            break loopfora;  //Encerra o aplicativo
                                        default:
                                            System.out.println("Opcao invalida!");
                                            break;
                                    }
                            } while (select2 != 0);
                        } else {
                            System.out.println("Acesso negado!");
                        }
                        break;

                        
                    case 2:
                        {
                            //boolean duplicada;
                            User u = new User();
                            System.out.print("Digite o seu nome de usuario: ");
                            u.setName(sc.nextLine());
                            /*duplicada = userRepo.get(u.getName()).equals(u);        funcao para identificar nomes de usuario duplicados (nao funciona)
                            while(duplicada == true){
                                System.out.println("Nome invalido, tente novamente!");
                                System.out.println("Digite o seu nome de usuario: ");
                                u.setName(sc.nextLine());
                                duplicada = userRepo.get(u.getName()).equals(u);
                            }*/
                            System.out.print("Digite sua senha: ");
                            u.setPassword(sc.nextLine());
                            System.out.print("Digite o seu nome completo: ");
                            u.setFullName(sc.nextLine());
                            System.out.print("Digite o seu email: ");
                            u.setEmail(sc.nextLine());
                            userRepo.save(u);
                            System.out.print("Cadastro efetuado!");
                            break;
                        }  //executa o cadastro de um novo usuario
                        
                    case 0:
                        break;
                        
                    default:
                        System.out.print("Opcao invalida!");
                        break;
                }
        } while (select != 0);

        client.shutdown();
      } catch (Exception e) {
        e.printStackTrace();
        }
    }
}
