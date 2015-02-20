#
# Autogenerated by Thrift Compiler (0.9.0)
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#
require 5.6.0;
use strict;
use warnings;
use Thrift;

use thrift::adept::common::Types;
use thrift::adept::common::ChunkService;

# HELPER FUNCTIONS AND STRUCTURES

package thrift::adept::common::CommittedBeliefService_getModality_args;
use base qw(Class::Accessor);

sub new {
                                                                          my $classname = shift;
                                                                          my $self      = {};
                                                                          my $vals      = shift || {};
                                                                          return bless ($self, $classname);
}

sub getName {
                                                                          return 'CommittedBeliefService_getModality_args';
                                                                        }

sub read {
                                                                          my ($self, $input) = @_;
                                                                          my $xfer  = 0;
                                                                          my $fname;
                                                                          my $ftype = 0;
                                                                          my $fid   = 0;
                                                                          $xfer += $input->readStructBegin(\$fname);
                                                                          while (1) 
                                                                          {
                                                                            $xfer += $input->readFieldBegin(\$fname, \$ftype, \$fid);
                                                                            if ($ftype == TType::STOP) {
                                                                              last;
                                                                            }
                                                                            SWITCH: for($fid)
                                                                            {
                                                                                $xfer += $input->skip($ftype);
                                                                            }
                                                                            $xfer += $input->readFieldEnd();
                                                                          }
                                                                          $xfer += $input->readStructEnd();
                                                                          return $xfer;
                                                                        }

sub write {
                                                                          my ($self, $output) = @_;
                                                                          my $xfer   = 0;
                                                                          $xfer += $output->writeStructBegin('CommittedBeliefService_getModality_args');
                                                                          $xfer += $output->writeFieldStop();
                                                                          $xfer += $output->writeStructEnd();
                                                                          return $xfer;
                                                                        }

package thrift::adept::common::CommittedBeliefService_getModality_result;
use base qw(Class::Accessor);
thrift::adept::common::CommittedBeliefService_getModality_result->mk_accessors( qw( success ) );

sub new {
                                                                          my $classname = shift;
                                                                          my $self      = {};
                                                                          my $vals      = shift || {};
                                                                          $self->{success} = undef;
                                                                          if (UNIVERSAL::isa($vals,'HASH')) {
                                                                            if (defined $vals->{success}) {
                                                                              $self->{success} = $vals->{success};
                                                                            }
                                                                          }
                                                                          return bless ($self, $classname);
}

sub getName {
                                                                          return 'CommittedBeliefService_getModality_result';
                                                                        }

sub read {
                                                                          my ($self, $input) = @_;
                                                                          my $xfer  = 0;
                                                                          my $fname;
                                                                          my $ftype = 0;
                                                                          my $fid   = 0;
                                                                          $xfer += $input->readStructBegin(\$fname);
                                                                          while (1) 
                                                                          {
                                                                            $xfer += $input->readFieldBegin(\$fname, \$ftype, \$fid);
                                                                            if ($ftype == TType::STOP) {
                                                                              last;
                                                                            }
                                                                            SWITCH: for($fid)
                                                                            {
                                                                              /^0$/ && do{                                                                              if ($ftype == TType::I32) {
                                                                                $xfer += $input->readI32(\$self->{success});
                                                                              } else {
                                                                                $xfer += $input->skip($ftype);
                                                                              }
                                                                              last; };
                                                                                $xfer += $input->skip($ftype);
                                                                            }
                                                                            $xfer += $input->readFieldEnd();
                                                                          }
                                                                          $xfer += $input->readStructEnd();
                                                                          return $xfer;
                                                                        }

sub write {
                                                                          my ($self, $output) = @_;
                                                                          my $xfer   = 0;
                                                                          $xfer += $output->writeStructBegin('CommittedBeliefService_getModality_result');
                                                                          if (defined $self->{success}) {
                                                                            $xfer += $output->writeFieldBegin('success', TType::I32, 0);
                                                                            $xfer += $output->writeI32($self->{success});
                                                                            $xfer += $output->writeFieldEnd();
                                                                          }
                                                                          $xfer += $output->writeFieldStop();
                                                                          $xfer += $output->writeStructEnd();
                                                                          return $xfer;
                                                                        }

package thrift::adept::common::CommittedBeliefService_getSequenceId_args;
use base qw(Class::Accessor);

sub new {
                                                                          my $classname = shift;
                                                                          my $self      = {};
                                                                          my $vals      = shift || {};
                                                                          return bless ($self, $classname);
}

sub getName {
                                                                          return 'CommittedBeliefService_getSequenceId_args';
                                                                        }

sub read {
                                                                          my ($self, $input) = @_;
                                                                          my $xfer  = 0;
                                                                          my $fname;
                                                                          my $ftype = 0;
                                                                          my $fid   = 0;
                                                                          $xfer += $input->readStructBegin(\$fname);
                                                                          while (1) 
                                                                          {
                                                                            $xfer += $input->readFieldBegin(\$fname, \$ftype, \$fid);
                                                                            if ($ftype == TType::STOP) {
                                                                              last;
                                                                            }
                                                                            SWITCH: for($fid)
                                                                            {
                                                                                $xfer += $input->skip($ftype);
                                                                            }
                                                                            $xfer += $input->readFieldEnd();
                                                                          }
                                                                          $xfer += $input->readStructEnd();
                                                                          return $xfer;
                                                                        }

sub write {
                                                                          my ($self, $output) = @_;
                                                                          my $xfer   = 0;
                                                                          $xfer += $output->writeStructBegin('CommittedBeliefService_getSequenceId_args');
                                                                          $xfer += $output->writeFieldStop();
                                                                          $xfer += $output->writeStructEnd();
                                                                          return $xfer;
                                                                        }

package thrift::adept::common::CommittedBeliefService_getSequenceId_result;
use base qw(Class::Accessor);
thrift::adept::common::CommittedBeliefService_getSequenceId_result->mk_accessors( qw( success ) );

sub new {
                                                                          my $classname = shift;
                                                                          my $self      = {};
                                                                          my $vals      = shift || {};
                                                                          $self->{success} = undef;
                                                                          if (UNIVERSAL::isa($vals,'HASH')) {
                                                                            if (defined $vals->{success}) {
                                                                              $self->{success} = $vals->{success};
                                                                            }
                                                                          }
                                                                          return bless ($self, $classname);
}

sub getName {
                                                                          return 'CommittedBeliefService_getSequenceId_result';
                                                                        }

sub read {
                                                                          my ($self, $input) = @_;
                                                                          my $xfer  = 0;
                                                                          my $fname;
                                                                          my $ftype = 0;
                                                                          my $fid   = 0;
                                                                          $xfer += $input->readStructBegin(\$fname);
                                                                          while (1) 
                                                                          {
                                                                            $xfer += $input->readFieldBegin(\$fname, \$ftype, \$fid);
                                                                            if ($ftype == TType::STOP) {
                                                                              last;
                                                                            }
                                                                            SWITCH: for($fid)
                                                                            {
                                                                              /^0$/ && do{                                                                              if ($ftype == TType::I64) {
                                                                                $xfer += $input->readI64(\$self->{success});
                                                                              } else {
                                                                                $xfer += $input->skip($ftype);
                                                                              }
                                                                              last; };
                                                                                $xfer += $input->skip($ftype);
                                                                            }
                                                                            $xfer += $input->readFieldEnd();
                                                                          }
                                                                          $xfer += $input->readStructEnd();
                                                                          return $xfer;
                                                                        }

sub write {
                                                                          my ($self, $output) = @_;
                                                                          my $xfer   = 0;
                                                                          $xfer += $output->writeStructBegin('CommittedBeliefService_getSequenceId_result');
                                                                          if (defined $self->{success}) {
                                                                            $xfer += $output->writeFieldBegin('success', TType::I64, 0);
                                                                            $xfer += $output->writeI64($self->{success});
                                                                            $xfer += $output->writeFieldEnd();
                                                                          }
                                                                          $xfer += $output->writeFieldStop();
                                                                          $xfer += $output->writeStructEnd();
                                                                          return $xfer;
                                                                        }

package thrift::adept::common::CommittedBeliefServiceIf;

use strict;
use base qw(thrift::adept::common::ChunkServiceIf);

sub getModality{
  my $self = shift;

  die 'implement interface';
}

sub getSequenceId{
  my $self = shift;

  die 'implement interface';
}

package thrift::adept::common::CommittedBeliefServiceRest;

use strict;
use base qw(thrift::adept::common::ChunkServiceRest);

sub getModality{
                                                                          my ($self, $request) = @_;

                                                                          return $self->{impl}->getModality();
                                                                        }

sub getSequenceId{
                                                                          my ($self, $request) = @_;

                                                                          return $self->{impl}->getSequenceId();
                                                                        }

package thrift::adept::common::CommittedBeliefServiceClient;

use base qw(thrift::adept::common::ChunkServiceClient);
use base qw(thrift::adept::common::CommittedBeliefServiceIf);
sub new {
                                                                          my ($classname, $input, $output) = @_;
                                                                          my $self      = {};
                                                                          $self = $classname->SUPER::new($input, $output);
                                                                          return bless($self,$classname);
}

sub getModality{
  my $self = shift;

                                                                                                                                                    $self->send_getModality();
                                                                          return $self->recv_getModality();
}

sub send_getModality{
  my $self = shift;

                                                                          $self->{output}->writeMessageBegin('getModality', TMessageType::CALL, $self->{seqid});
                                                                          my $args = new thrift::adept::common::CommittedBeliefService_getModality_args();
                                                                          $args->write($self->{output});
                                                                          $self->{output}->writeMessageEnd();
                                                                          $self->{output}->getTransport()->flush();
}

sub recv_getModality{
  my $self = shift;

                                                                          my $rseqid = 0;
                                                                          my $fname;
                                                                          my $mtype = 0;

                                                                          $self->{input}->readMessageBegin(\$fname, \$mtype, \$rseqid);
                                                                          if ($mtype == TMessageType::EXCEPTION) {
                                                                            my $x = new TApplicationException();
                                                                            $x->read($self->{input});
                                                                            $self->{input}->readMessageEnd();
                                                                            die $x;
                                                                          }
                                                                          my $result = new thrift::adept::common::CommittedBeliefService_getModality_result();
                                                                          $result->read($self->{input});
                                                                          $self->{input}->readMessageEnd();

                                                                          if (defined $result->{success} ) {
                                                                            return $result->{success};
                                                                          }
                                                                          die "getModality failed: unknown result";
}
sub getSequenceId{
  my $self = shift;

                                                                                                                                                    $self->send_getSequenceId();
                                                                          return $self->recv_getSequenceId();
}

sub send_getSequenceId{
  my $self = shift;

                                                                          $self->{output}->writeMessageBegin('getSequenceId', TMessageType::CALL, $self->{seqid});
                                                                          my $args = new thrift::adept::common::CommittedBeliefService_getSequenceId_args();
                                                                          $args->write($self->{output});
                                                                          $self->{output}->writeMessageEnd();
                                                                          $self->{output}->getTransport()->flush();
}

sub recv_getSequenceId{
  my $self = shift;

                                                                          my $rseqid = 0;
                                                                          my $fname;
                                                                          my $mtype = 0;

                                                                          $self->{input}->readMessageBegin(\$fname, \$mtype, \$rseqid);
                                                                          if ($mtype == TMessageType::EXCEPTION) {
                                                                            my $x = new TApplicationException();
                                                                            $x->read($self->{input});
                                                                            $self->{input}->readMessageEnd();
                                                                            die $x;
                                                                          }
                                                                          my $result = new thrift::adept::common::CommittedBeliefService_getSequenceId_result();
                                                                          $result->read($self->{input});
                                                                          $self->{input}->readMessageEnd();

                                                                          if (defined $result->{success} ) {
                                                                            return $result->{success};
                                                                          }
                                                                          die "getSequenceId failed: unknown result";
}
package thrift::adept::common::CommittedBeliefServiceProcessor;

use strict;
use base qw(thrift::adept::common::ChunkServiceProcessor);

sub process {
                                                                            my ($self, $input, $output) = @_;
                                                                            my $rseqid = 0;
                                                                            my $fname  = undef;
                                                                            my $mtype  = 0;

                                                                            $input->readMessageBegin(\$fname, \$mtype, \$rseqid);
                                                                            my $methodname = 'process_'.$fname;
                                                                            if (!$self->can($methodname)) {
                                                                              $input->skip(TType::STRUCT);
                                                                              $input->readMessageEnd();
                                                                              my $x = new TApplicationException('Function '.$fname.' not implemented.', TApplicationException::UNKNOWN_METHOD);
                                                                              $output->writeMessageBegin($fname, TMessageType::EXCEPTION, $rseqid);
                                                                              $x->write($output);
                                                                              $output->writeMessageEnd();
                                                                              $output->getTransport()->flush();
                                                                              return;
                                                                            }
                                                                            $self->$methodname($rseqid, $input, $output);
                                                                            return 1;
}

sub process_getModality {
                                                                            my ($self, $seqid, $input, $output) = @_;
                                                                            my $args = new thrift::adept::common::CommittedBeliefService_getModality_args();
                                                                            $args->read($input);
                                                                            $input->readMessageEnd();
                                                                            my $result = new thrift::adept::common::CommittedBeliefService_getModality_result();
                                                                            $result->{success} = $self->{handler}->getModality();
                                                                            $output->writeMessageBegin('getModality', TMessageType::REPLY, $seqid);
                                                                            $result->write($output);
                                                                            $output->writeMessageEnd();
                                                                            $output->getTransport()->flush();
}

sub process_getSequenceId {
                                                                            my ($self, $seqid, $input, $output) = @_;
                                                                            my $args = new thrift::adept::common::CommittedBeliefService_getSequenceId_args();
                                                                            $args->read($input);
                                                                            $input->readMessageEnd();
                                                                            my $result = new thrift::adept::common::CommittedBeliefService_getSequenceId_result();
                                                                            $result->{success} = $self->{handler}->getSequenceId();
                                                                            $output->writeMessageBegin('getSequenceId', TMessageType::REPLY, $seqid);
                                                                            $result->write($output);
                                                                            $output->writeMessageEnd();
                                                                            $output->getTransport()->flush();
}

1;