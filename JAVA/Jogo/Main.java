import entidades.Itens;
import entidades.inimigos.Bandido;
import entidades.inimigos.Enemy;
import entidades.inimigos.Goblins;
import entidades.inimigos.Rei;
import entidades.inimigos.Yeti;
import entidades.jogador.Assasino;
import entidades.jogador.Berseker;
import entidades.jogador.Guerreiro;
import entidades.jogador.Mago;
import entidades.jogador.Player;
import entidades.npcs.Comerciante;
import entidades.npcs.QuestGiver;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int jogar = 1;

        while (jogar == 1) {
            String nome;
            int caminhos;

            limparTela();
            System.out.println("======As Profundezas de Drakar======\n");
            System.out.print("Insira seu nome: ");
            nome = sc.next();
            sc.nextLine();
            System.out.println("Escolha sua classe:");
            System.out.println("(1) Guerreiro");
            System.out.println("(2) Mago");
            System.out.println("(3) Berseker");
            System.out.println("(4) Assasino");
            System.out.print("Escolha: ");
            int escolhaClasse = sc.nextInt();
            sc.nextLine();

            Player jogador;
            switch (escolhaClasse) {
                case 1: jogador = new Guerreiro(nome);
                System.out.println("Voce tem uma espada em uma mao e um escudo na outra, sem erro."); 
                break;
                case 2: jogador = new Mago(nome); 
                System.out.println("Voce tem barba branca e um cajado, obvio.");
                break;
                case 3: jogador = new Berseker(nome); 
                System.out.println("Muito musculo, pouco cerebro.");
                break;
                case 4: jogador = new Assasino(nome); 
                System.out.println("Agilidade e furtividade, voce tem uma capa cobrindo o rosto.");
                break;
                default: 
                    System.out.println("Opção invalida, escolhendo Guerreiro por padrao.");
                    jogador = new Guerreiro(nome);
                    break;
            }

            limparTela();

            System.out.println("\nOla, " + "" + nome + "" + ", voce e um aventureiro no vasto mundo de Drakar, um lugar repleto de magia e misterios, \nonde lendas ganham vida e perigos espreitam nas sombras."); 
            System.out.println("Hoje, sua missao e explorar a temida Dungeon da Caveira Carmesim, um antigo labirinto repleto de armadilhas, \ncriaturas sombrias e segredos esquecidos.");
            System.out.println("Dizem que, em suas profundezas, repousa o tesouro perdido dos reis antigos — e apenas os mais corajosos ousam busca-lo.");
            System.out.println("Voce estava prestes a deixar sua vila e ir em direcao a sua aventura quando avistou duas figuras proximas a entrada da vila, \num comerciante com todo tipo de equipamento e um morador da vila que parece desesperado por ajuda.");
            System.out.println("O que voce vai fazer?");

            Comerciante comerciante = null;
            QuestGiver questGiver = null;
            boolean questAceita = false;

            boolean naVila = true;
            do {
                limparTela();
                System.out.println("(1) Falar com comerciante");
                System.out.println("(2) Falar com morador");
                if (questAceita && !jogador.isEncontroBConcluido()) {
                    System.out.println("(3) Ir para a floresta (missao)");
                }
                System.out.println("(4) Ir para a dungeon");
                System.out.println("(5) Menu");
                System.out.print("Escolha: ");
                int escolhaVila = sc.nextInt();
                sc.nextLine();

                switch (escolhaVila) {
                    case 1:
                    List<Itens> itensLoja = new ArrayList<>();
                        itensLoja.add(new Itens("Armadura de Couro", "resistencia", 3, 30));
                        itensLoja.add(new Itens("Espada longa", "forca", 4, 20));
                        itensLoja.add(new Itens("Chapeu de feiticeiro", "inteligencia", 2, 15));
                        itensLoja.add(new Itens("Botas leves", "agilidade", 3, 25));
                        comerciante = new Comerciante("Comerciante", itensLoja);
                        System.out.println("Ah, bem vindo a minha loja aventureiro! Gostaria de novos equipamentos?");
                        int opcaoLoja;
                        do {
                            System.out.println("\nSeu ouro: " + jogador.getOuro());
                            List<Itens> itens = comerciante.getItensDisponiveis();
                            for (int i = 0; i < itens.size(); i++) {
                                Itens item = itens.get(i);
                                System.out.println("(" + (i + 1) + ") " + item.getNome() + " - " + item.getPreco() + " de ouro (+" + item.getValorModificador() + " " + item.getAtributoModificado() + ")");
                            }
                            System.out.println("(0) Sair da loja");
                            System.out.print("Escolha: ");
                            opcaoLoja = sc.nextInt();
                            sc.nextLine();

                            if (opcaoLoja >= 1 && opcaoLoja <= itens.size()) {
                                boolean comprou = comerciante.venderItem(jogador, opcaoLoja - 1);
                                if (comprou) {
                                    System.out.println("Compra realizada!");
                                }
                            }
                        } while (opcaoLoja != 0);
                        break;
                    case 2:
                    questGiver = new QuestGiver("Morador", 50);
                        System.out.println("Oh ceus, que bom que voce esta aqui aventureiro, um grupo de bandidos acabou de saquear a vila \ne correu para a floresta! Por favor nos ajude!");
                        System.out.println("(1) Aceitar missao");
                        System.out.println("(2) Recusar");
                        System.out.print("Escolha: ");
                        int respostaQuest = sc.nextInt();
                        sc.nextLine();

                        if (respostaQuest == 1) {
                            questAceita = true;
                            System.out.println("Muito obrigado, aventureiro! Encontre os bandidos na floresta.");
                        } else {
                            questGiver = null;
                            System.out.println("Entendo... boa sorte na sua jornada, então.");
                        }
                        break;
                    case 3:
                        if (questAceita && !jogador.isEncontroBConcluido()) {
                            if (questAceita && !jogador.isEncontroBConcluido()) {
                                Bandido bandido = new Bandido("bandido");
                                boolean venceu = combate(jogador, bandido);
                                if (venceu) {
                                    jogador.marcarEncontroBConcluido();
                                    if (questGiver != null) {
                                        questGiver.entregarRecompensa(jogador);
                                        System.out.println("O morador te recompensa com ouro!");
                                    }
                                } else {
                                    jogar = 0;
                                }
                            } else {
                                System.out.println("Não ha nada pra fazer na floresta agora.");
                            }
                        } else {
                            System.out.println("Não ha nada pra fazer na floresta agora.");
                        }
                        break;
                    case 4:
                        naVila = false;
                        System.out.println("Você decide seguir direto para a dungeon.");
                        break;
                    case 5: 
                        menuStatus(jogador);
                        break;
                    default:
                        System.out.println("Opção invalida.");
                        break;
                }
            } while (naVila);

            System.out.print("\nAperte ENTER para continuar...");
            sc.nextLine();

            System.out.println("Agora se sentindo confiante e com fome de aventura, voce parte em direcao a localizacao \nda Dungeon da Caveira Carmesim, pronto para enfrentar qualquer desafio.");
            System.out.printf("%nLogo ao chegar, %s se depara com um portal de pedra coberto por musgo e simbolos runicos quase apagados pelo tempo.%n", nome);
            System.out.println("No alto do arco de entrada, uma caveira de pedra manchada de vermelho parece observa-lo.");

            System.out.print("\nPressione ENTER para entrar na caverna...");
            sc.nextLine();

            System.out.printf("%nSem hesitar, %s adentra o local umido e sombrio, levando consigo uma tocha que estava presa a parede.%n", nome);
            System.out.println("De repente, o caminho se divide em tres passagens.");
            System.out.print("\nAperte ENTER para continuar...");
            sc.nextLine();

            int volt = 1;
            int prox2 = 0;

            do {
                limparTela();
                System.out.println("\nA esquerda, um corredor estreito e gelado.");
                System.out.println("A frente, ruinas antigas com um leve brilho.");
                System.out.println("A direita, uma névoa espessa cobre o chao.");

                System.out.print("\nEscolha um caminho:\n(1) Esquerda\n(2) Centro\n(3) Direita\n(4) Menu\nEscolha: ");
                caminhos = lerInt();

                switch (caminhos) {

                    case 1: {
                        limparTela();
                        System.out.println("\nPassagem da esquerda: Voce segue por corredor frio, estreito, e consegue ouvir um barulho de correntes vindo do final dele.");
                        System.out.print("\nPressione ENTER para continuar...");
                        sc.nextLine();

                        System.out.println("\nAo chegar ao final do corredor voce se depara com uma gigante criatura de olhos vermelhos e famintos, \npelo branco manchado de sangue e dentes afiados que ela exibe a cada rugido de raiva.");
                        System.out.println("O Yeti esta acorrentado a parede, mas as correntes parecem enferrujadas, prestes a quebrar.");
                        System.out.print("\nESCOLHA!\n(1) LUTAR\n(2) CORRER\nEscolha: ");
                        int escolha = lerInt();

                        if (escolha == 1) {
                            System.out.println("Assim que o Yeti nota sua presenca ele levanta os bracos violentamente, se libertando das correntes e \n4indo em sua direcao a toda velocidade!");

                            Yeti yeti = new Yeti("Yeti");
                            boolean venceu = combate(jogador, yeti);

                            if (venceu) {
                                System.out.printf("%n%s desfere o golpe final, e o Yeti tomba com um rugido agonizante.%n", nome);
                                System.out.print("\nPressione ENTER para continuar...");
                                sc.nextLine();
                                volt = 0;
                            } else {
                                System.out.printf("%n%s tenta lutar, mas o monstro e rapido e poderoso.%n", nome);
                                System.out.println("Um golpe certeiro atinge seu peito...\nGAME OVER.");
                                System.out.print("\nAperte 0 para encerrar ou 1 para recomeçar: ");
                                jogar = lerInt();
                                volt = 0;
                            }
                        } else if (escolha == 2) {
                            System.out.printf("%nAterrorizado pelo monstro, %s corre de volta a encruzilhada. Ofegante e ainda tremendo, voce se ve de volta ao lugar de onde saiu.%n", nome);
                            System.out.print("\nPressione ENTER para continuar...");
                            sc.nextLine();
                            volt = 1;
                        } else {
                            System.out.println("\nOpção invalida!");
                            volt = 1;
                        }
                        break;
                    }

                    case 2: {
                        limparTela();
                        prox2 = 0;

                        do {
                            System.out.printf("%nAo escolher a passagem do meio, %s segue por uma sequencia de corredores estreitos ate que o caminho \nse abre em um salao amplo e abafado. %nO ar ali e pesado, repleto do cheiro de fumaça e sujeira.%nAntes que possa reagir, figuras pequenas e ageis emergem das sombras — goblins, de pele esverdeada e olhos %namarelados, que o cercam rapidamente com risadas estridentes.%nCada um empunha uma adaga enferrujada ou um pedaço de madeira %nlascada, prontos para atacar o intruso que ousou invadir seu territorio.%n", nome);
                            System.out.print("\nESCOLHA!\n(1) LUTAR\n(2) SUBORNAR\nEscolha: ");
                            int escolha2 = lerInt();

                            if (escolha2 == 1) {
                                boolean venceu = combateGoblins(jogador, 3);
                                if (venceu) {
                                    System.out.printf("Os inimigos atacam em meio a gritos e caos, mas o aventureiro reage com precisao. Com alguns arranhoes e o \ncoraçao acelerado, %s permanece de pe entre os corpos das criaturas.%nO salao volta ao silencio, e logo a frente uma pequena passagem levando a outro comodo.%n", nome);
                                    prox2 = 1;
                                } else {
                                    System.out.println("Os goblins atacam sem parar, cada vez mais e mais deles emergem das sombras de maneira que parece nunca acabar. Uma adaga perfura suas costas, outra perfura sua perna, e eventualmente os goblins conseguem te derrubar...\nGAME OVER.");
                                    System.out.print("\nAperte 0 para encerrar ou 1 para recomeçar: ");
                                    jogar = lerInt();
                                    prox2 = 0;
                                }
                                System.out.print("\nPressione ENTER para continuar...");
                                sc.nextLine();
                            } else if (escolha2 == 2) {
                                if (jogador.getOuro() >= 10) {
                                    jogador.gastarOuro(10);
                                    System.out.printf("%n%s cercado por inimigos, busca desesperadamente uma saida. O som dos passos e risadas dos goblins ecoa pelo %nsalao, enquanto as criaturas se aproximam cada vez mais.%nEntao, uma ideia surge. Ele se lembra do pequeno saco de moedas de ouro que carrega consigo — %nsua ultima esperanca. Com um movimento rapido, %s arremessa o saco no chao diante dos goblins.%nAs criaturas param, trocam olhares desconfiados e, ao verem o brilho do ouro, comecam a disputar o %nsaque com grunhidos gananciosos. %nEm poucos segundos, a tensao se desfaz — os goblins se dispersam, satisfeitos com o tesouro facil.%nNo silencio que se segue, %s percebe uma estreita passagem oculta no fundo da sala, agora revelada pela %nconfusao. Era a chance perfeita para escapar.%n", nome, nome, nome);
                                    System.out.print("\nPressione ENTER para continuar...");
                                    sc.nextLine();
                                    prox2 = 1;
                                } else {
                                    System.out.printf("%n%s cercado por inimigos, busca desesperadamente uma saida. O som dos passos e risadas dos goblins ecoa pelo %nsalao, enquanto as criaturas se aproximam cada vez mais.%nEntao, uma ideia surge. Ele se lembra do pequeno saco de moedas de ouro que carrega consigo. Com um movimento rapido, %s arremessa o saco no chao diante dos goblins. No entanto, eles nao parecem satisfeitos com a quantia e partem pra briga mesmo assim.", nome, nome);
                                    System.out.print("\nPressione ENTER para continuar...");
                                    sc.nextLine();

                                    boolean venceu = combateGoblins(jogador, 3);
                                    if (venceu) {
                                    prox2 = 1;
                                    } else {
                                        System.out.println("\nOs goblins atacam sem parar, cada vez mais e mais deles emergem das sombras de maineira que parece nunca acabar. Uma adaga perfura suas costas, outra perfura sua perna, e eventualmente os goblins conseguem te derrubar...\nGAME OVER.");
                                        System.out.print("\nAperte 0 para encerrar ou 1 para recomeçar: ");
                                        jogar = lerInt();
                                        prox2 = 0;
                                    }
                                }
                            } else {
                                System.out.println("\nOpçao incorreta! Tente novamente.");
                                System.out.print("Pressione ENTER para tentar novamente...");
                                sc.nextLine();
                            }
                        } while (prox2 == 0);

                        volt = 0;
                        break;
                    }

                    case 3: {
                        limparTela();
                        System.out.printf("%nEscolhendo a passagem da direita, %s avança por um corredor de pedra que parece se estender eternamente.%nA chama da tocha tremula, projetando sombras distorcidas nas paredes umidas, enquanto o ar se torna cada vez mais denso e abafado. %nApos quase uma hora de caminhada, o tunel termina abruptamente em uma parede solida de rocha. %nNenhum som, nenhuma abertura — apenas o eco dos proprios passos retornando do vazio. Parece que %s não escolheu o caminho certo.%n", nome, nome);
                        System.out.print("\nVolte para tras e escolha outro caminho...");
                        sc.nextLine();
                        volt = 1;
                        break;
                    }

                    case 4: 
                        menuStatus(jogador);
                        break;

                    default:
                        System.out.println("\nOpçao invalida. Escolha 1, 2 ou 3.");
                        sc.nextLine();
                        volt = 1;
                        break;
                }

            } while (volt == 1);

            if (prox2 == 1) {
                limparTela();
                System.out.printf("%n%s chega a uma nova camara com uma grande porta de marmore branco e uma cabeca de esfinge esculpida em pedra logo acima.%n", nome);
                System.out.print("\nPressione ENTER para continuar...");
                sc.nextLine();

                System.out.println("\nAo se aproximar da porta uma luz intensa inunda o ambiente revelando ossos espalhados pelo chao.");
                System.out.println("\nOs olhos de pedra da esfinge brilham em um tom ambar, e o ar parece ficar mais denso. Entao, uma voz misteriosa pronuncia um enigma:\n'Sou algo que todos carregam, Mas ninguem pode ver; Cresco sem parar, Mas nunca posso crescer; \nSou o comeco de tudo, E tambem o fim de cada passo. Diga-me… o que sou'");

                int acertou = 0;
                boolean chegouAoFinal = false;

                do {
                    System.out.println("\nEscolha sua resposta:");
                    System.out.println("(1) O Pensamento\n(2) O Tempo\n(3) A Sombra");
                    System.out.println("\n Ou aperte 4 para ir pro Menu");
                    int enigma = lerInt();

                    switch (enigma) {
                        case 1:
                            limparTela();
                            System.out.printf("%n\"INCORRETO\", ecoa a voz da esfinge, reverberando por todas as paredes do salao.%nDe repente, seus olhos de pedra emanam uma luz intensa e penetrante, tao forte que cega. Uma rajada de energia pura dispara do olhar da criatura, atingindo %s com força esmagadora.%nEm um instante, o aventureiro e desintegrado, restando apenas ossos espalhados pelo chao, como lembrança silenciosa de sua falha diante do enigma, e o salao retorna a quieta escuridao de antes.%n", nome);
                            System.out.println("\nGAME OVER!");
                            System.out.print("\nAperte 0 para encerrar ou 1 para recomeçar: ");
                            jogar = lerInt();
                            acertou = 1;
                            break;

                        case 2:
                            System.out.println("\nDe repente, o chao treme suavemente, e uma vibracao percorre as paredes antigas.");
                            System.out.println("Lentamente, as imponentes portas de marmore comecam a se abrir com um rangido profundo, \nrevelando um comodo oculto ainda maior que o anterior.");
                            System.out.println("No centro, um trono, e sentado nele, um esqueleto de caveira vermelha com um bau no colo e uma coroa na cabeca parece proteger o tesouro com os bracos.");
                            System.out.print("\nPressione ENTER para abrir o bau...");
                            sc.nextLine();

                            System.out.println("Antes que voce pudesse se aproximar, de repente uma luz vermelha acende nos olhos da caveira, \ne o esqueleto, agora em pe, pega uma gigantesca espada ao lado do trono, erguendo ela em sua direcao como se a espada tivesse o peso de uma pena. \nO Rei nao deixara que levem seu tesouro tao facilmente!");

                            if (jogador.getInteligencia() >= 10) {
                                System.out.println("Gracas a sua proeminente inteligencia e profundo conhecimento em magia, voce rapidamente percebe que a \nfonte do poder desse esqueleto que faz ele se mecher esta concentrada na coroa que ele usa.");
                                System.out.print("\n(1) ATACAR COROA!\nEscolha: ");
                                lerInt();
                                chegouAoFinal = true;
                            } else {
                                Rei rei = new Rei("Rei Carmesin");
                                boolean venceu = combate(jogador, rei);
                                if (venceu) {
                                    chegouAoFinal = true;
                                } else {
                                    System.out.println("\nO Rei Carmesim golpeia com força esmagadora, e tudo se apaga...\nGAME OVER.");
                                    System.out.print("\nAperte 0 para encerrar ou 1 para recomeçar: ");
                                    jogar = lerInt();
                                }
                            }
                            acertou = 1;
                            break;

                        case 3:
                            limparTela();
                            System.out.printf("%n\"INCORRETO\", ecoa a voz da esfinge, reverberando por todas as paredes do salao.%nDe repente, seus olhos de pedra emanam uma luz intensa e penetrante, tao forte que cega. Uma rajada de energia pura dispara do olhar da criatura, atingindo %s com força esmagadora.%nEm um instante, o aventureiro e desintegrado, restando apenas ossos espalhados pelo chao, como lembrança silenciosa de sua falha diante do enigma, e o salao retorna a quieta escuridao de antes.%n", nome);
                            System.out.println("\nGAME OVER!");
                            System.out.print("\nAperte 0 para encerrar ou 1 para recomeçar: ");
                            jogar = lerInt();
                            acertou = 1;
                            break;
                        
                        case 4: 
                        menuStatus(jogador);
                        break;

                        default:
                            System.out.println("\nOpçao invalida! Escolha 1, 2 ou 3.");
                            break;
                    }
                } while (acertou == 0);

                if (chegouAoFinal) {
                    limparTela();
                    System.out.println("A coroa atinge o chao com um som metalico que ecoa por todo o salao, e imediatamente o esqueleto para de se mover, \nvoltando a ser uma mera pilha de ossos. Voce suspira aliviado agora que tudo acabou, mas seu alivio dura pouco. De repente, uma voz grave e assustadora \npreenche sua mente, ela parece vir de todo lugar e lugar nenhum ao mesmo tempo.");
                    System.out.println("'HUMANO TOLO, GANANCIOSO, DESEJA TANTO ESSE TESOURO? PODE FICAR! SUA ALMA AGORA NAO TERA DESCANSO \nENQUANTO OUTRO NAO VIER E ASSUMIR O SEU LUGAR!'");
                    System.out.println("Voce sente sua consciencia se esvair, seu corpo parece nao pertencer mais a voce, como se algo tivesse o \npossuido e assumido o controle. Lentamente, contra sua vontade, voce caminha ate o trono, coloca a coroa na sua cabeca, e se senta com o bau do \ntesouro no seu colo, voce nem sequer sabe o que tem dentro dele, mas voce sente uma vontade inexplicavel de proteger o bau.");
                    limparTela();
                    System.out.println("Bem vindo de volta, Rei Carmesim.");
                }
            }

            if (jogar != 0) {
                System.out.printf("%nA aventura termina aqui, aventureiro %s!%n", nome);
                System.out.print("Aperte 0 para encerrar ou 1 para jogar novamente: ");
                jogar = lerInt();
            }
        }   

        System.out.println("\nFim da jornada. Ate a próxima, aventureiro!");
        sc.close();
    }

    static void menuStatus(Player jogador) {
        boolean noMenu = true;
        do {
            limparTela();
            System.out.println("\n=== Status de " + jogador.getNome() + " ===");
            System.out.println("Nivel: " + jogador.getLevel());
            System.out.println("Vida: " + jogador.getVidaAtual() + "/" + jogador.getVidaMaxima());
            System.out.println("Forca: " + jogador.getForca());
            System.out.println("Resistencia: " + jogador.getResistencia());
            System.out.println("Agilidade: " + jogador.getAgilidade());
            System.out.println("Inteligencia: " + jogador.getInteligencia());
            System.out.println("Ouro: " + jogador.getOuro());
            System.out.println("Pontos disponiveis: " + jogador.getPontosDisponiveis());

            if (jogador.getPontosDisponiveis() > 0) {
                System.out.println("\n(1) Investir em Forca");
                System.out.println("(2) Investir em Resistencia");
                System.out.println("(3) Investir em Agilidade");
                System.out.println("(4) Investir em Inteligencia");
            }
            System.out.println("(0) Voltar");
            System.out.print("Escolha: ");
            int escolha = sc.nextInt();
            sc.nextLine();

            switch (escolha) {
                case 1: jogador.investirPonto("forca"); 
                    break;
                case 2: jogador.investirPonto("resistencia"); 
                    break;
                case 3: jogador.investirPonto("agilidade"); 
                    break;
                case 4: jogador.investirPonto("inteligencia"); 
                    break;
                case 0: noMenu = false; 
                    break;
                default: System.out.println("Opcao invalida."); 
                    break;
            }
        } while (noMenu);
    }

    static int lerInt() {
        while (!sc.hasNextInt()) {
            sc.next();
        }
        int valor = sc.nextInt();
        sc.nextLine(); 
        return valor;
    }

    static void limparTela() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    static boolean lutar(Player jogador, Enemy inimigo) {
        Random rand = new Random();
        System.out.println("\nUm " + inimigo.getNome() + " aparece!");

        while (jogador.estaVivo() && inimigo.estaVivo()) {
            System.out.println("\nSua vida: " + jogador.getVidaAtual() + "/" + jogador.getVidaMaxima());
            System.out.println(inimigo.getNome() + " vida: " + inimigo.getVidaAtual() + "/" + inimigo.getVidaMaxima());
            System.out.println("(1) Atacar");
            System.out.println("(2) Defender");
            System.out.println("(3) Esquivar");
            if (jogador.temHabilidadeEspecial()) {
                System.out.println("(4) Habilidade especial (" + jogador.getManaAtual() + " mana)");
            }
            System.out.print("Escolha: ");
            int acao = sc.nextInt();
            sc.nextLine();

            boolean defendendo = false;
            boolean esquivou = false;

            switch (acao) {
                case 1:
                    int variacao = rand.nextInt(3) - 1;
                    int danoJogador = Math.max(1, jogador.getDano() + variacao);
                    inimigo.receberDano(danoJogador);
                    System.out.println("Voce causou " + danoJogador + " de dano!");
                    break;
                case 2:
                    defendendo = true;
                    System.out.println("Voce se prepara para bloquear o proximo golpe.");
                    break;
                case 3:
                    int chanceEsquiva = Math.min(75, jogador.getAgilidade() * 5);
                    esquivou = rand.nextInt(100) < chanceEsquiva;
                    System.out.println(esquivou ? "Voce se prepara pra esquivar!" : "Voce tenta esquivar...");
                    break;
                case 4:
                    if (jogador.temHabilidadeEspecial()) {
                        int danoEspecial = jogador.usarHabilidadeEspecial();
                        inimigo.receberDano(danoEspecial);
                        System.out.println("Voce conjura um ataque poderoso, causando " + danoEspecial + " de dano!");
                    } else {
                        System.out.println("Mana insuficiente!");
                    }
                    break;
                default:
                    System.out.println("Ação invalida, voce hesita e perde a vez.");
                    break;
            }

            if (inimigo.estaVivo()) {
                int danoInimigo = inimigo.getDano();
                if (defendendo) danoInimigo = danoInimigo / 2;
                if (esquivou) danoInimigo = 0;

                if (danoInimigo > 0) {
                    jogador.receberDano(danoInimigo);
                    System.out.println(inimigo.getNome() + " causou " + danoInimigo + " de dano em voce!");
                } else if (esquivou) {
                    System.out.println("Voce esquivou completamente do ataque!");
                } else {
                    System.out.println("Voce bloqueou o ataque!");
                }
            }
        }

        if (jogador.estaVivo()) {
            System.out.println("\nVoce derrotou " + inimigo.getNome() + "!");
            jogador.ganharNivel();
            return true;
        } else {
            System.out.println("\nVoce foi derrotado...");
            return false;
        }
    }

    static boolean combate(Player jogador, Enemy inimigo) {
        boolean venceu = lutar(jogador, inimigo);
        if (venceu) {
            System.out.println("\nVoce derrotou " + inimigo.getNome() + "!");
            jogador.ganharNivel();
        } else {
            System.out.println("\nVoce foi derrotado...");
        }
        return venceu;
    }

    static boolean combateGoblins(Player jogador, int quantidade) {
        for (int i = 1; i <= quantidade; i++) {
            System.out.println("\n--- Goblin " + i + " de " + quantidade + " ---");
            boolean venceu = lutar(jogador, new Goblins("Goblin"));
            if (!venceu) {
                System.out.println("\nVoce foi derrotado...");
                return false;
            }
        }
        System.out.println("\nVoce derrotou todos os goblins!");
        jogador.ganharNivel();
        return true;
    }
}
